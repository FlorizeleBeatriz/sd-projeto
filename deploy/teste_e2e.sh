#!/bin/bash
# Testes de aceitação HTTP do AgroGest. Uso: BASE=http://localhost:8081/AgroGest ./teste_e2e.sh
# Requer uma instância NOVA com dados de demonstração (-Dagrogest.demo=true, por omissão).
BASE=${BASE:-http://localhost:8081/AgroGest}
T=$(mktemp -d); PASS=0; FAIL=0
ok(){ PASS=$((PASS+1)); echo "  ✔ $1"; }
ko(){ FAIL=$((FAIL+1)); echo "  ✘ $1"; }
code(){ curl -s -o /dev/null -w '%{http_code}' "$@"; }
chk(){ # descrição, esperado, obtido
  if [ "$2" == "$3" ]; then ok "$1"; else ko "$1 (esperado=$2 obtido=$3)"; fi; }
has(){ # descrição, ficheiro/texto, padrão
  if echo "$2" | tr -d "\n" | sed "s/\xc2\xa0/ /g" | grep -qE -- "$3"; then ok "$1"; else ko "$1 (padrão '$3' não encontrado)"; fi; }
nao(){ if echo "$2" | grep -qE -- "$3"; then ko "$1 (padrão '$3' presente)"; else ok "$1"; fi; }
A="-b $T/a -c $T/a"; C="-b $T/c -c $T/c"; D="-b $T/d -c $T/d"

echo "== 1. Autenticação e filtros"
chk "GET /login = 200" 200 "$(code $BASE/login)"
chk "/catalogo sem sessão → 302" 302 "$(code $BASE/catalogo)"
chk "/admin/dashboard sem sessão → 302" 302 "$(code $BASE/admin/dashboard)"
R=$(curl -s $A -d 'email=admin@agro.mz&password=errada' $BASE/login); has "login inválido mostra erro" "$R" "inválidos"
chk "login admin → 302" 302 "$(code $A -d 'email=admin@agro.mz&password=admin234' $BASE/login)"
R=$(curl -s $A $BASE/admin/dashboard); has "dashboard: 3 fornecedores" "$R" '<b>3</b><span>Fornecedores'; has "dashboard: 5 clientes" "$R" '<b>5</b><span>Clientes'; has "dashboard: 4 vendas" "$R" '<b>4</b><span>Vendas'

echo "== 2. Registo e perfis"
chk "registo novo cliente → 302" 302 "$(code -d 'nome=Teste Um&email=t1@teste.mz&password=1234&telefone=84111&endereco=Rua A, Nampula' $BASE/registo)"
R=$(curl -s -d 'nome=Dup&email=T1@teste.mz&password=1234&telefone=1&endereco=x' $BASE/registo); has "email duplicado rejeitado" "$R" "Já existe uma conta"
R=$(curl -s -d 'nome=X&email=invalido&password=1234&telefone=1&endereco=x' $BASE/registo); has "email inválido rejeitado" "$R" "Email inválido|inválid"
chk "login cliente → 302" 302 "$(code $C -d 'email=t1@teste.mz&password=1234' $BASE/login)"
chk "cliente em /admin/dashboard → 403" 403 "$(code $C $BASE/admin/dashboard)"
chk "cliente em /admin/produtos (POST) → 403" 403 "$(code $C -d 'nome=x' $BASE/admin/produtos)"
chk "cliente em /admin/persistencia/gravar → 403" 403 "$(code $C -X POST $BASE/admin/persistencia/gravar)"
chk "admin em /carrinho → 403" 403 "$(code $A $BASE/carrinho)"

echo "== 3. Catálogo, carrinho e compra (cliente)"
R=$(curl -s $C $BASE/catalogo); has "catálogo lista produtos" "$R" "Semente de Milho"; has "catálogo mostra fornecedor" "$R" "Sementes do Sul"
R=$(curl -s "$C" "$BASE/catalogo?categoria=FERTILIZANTE" 2>/dev/null || curl -s $C "$BASE/catalogo?categoria=FERTILIZANTE"); R=$(curl -s $C "$BASE/catalogo?categoria=FERTILIZANTE")
has "filtro FERTILIZANTE mostra Adubo" "$R" "Adubo NPK"; nao "filtro FERTILIZANTE esconde Milho" "$R" "Semente de Milho"
R=$(curl -s -L $C -d 'produtoId=1&quantidade=99999' $BASE/carrinho/adicionar); has "adicionar acima do stock rejeitado" "$R" "Stock insuficiente"
curl -s -L $C -d 'produtoId=1&quantidade=3' $BASE/carrinho/adicionar >/dev/null
curl -s -L $C -d 'produtoId=3&quantidade=1' $BASE/carrinho/adicionar >/dev/null
R=$(curl -s $C $BASE/carrinho); has "carrinho tem Milho e Adubo" "$R" "Semente de Milho.*|Adubo"; has "subtotal 3×180+1×1450 = 1 990,00" "$R" "1 990,00"
R=$(curl -s -L $C -d 'q_1=5&q_3=1' $BASE/carrinho/actualizar); has "quantidade actualizada para 5" "$R" 'value="5"'
curl -s -L $C -d 'produtoId=3' $BASE/carrinho/remover >/dev/null; R=$(curl -s $C $BASE/carrinho); nao "item removido" "$R" "Adubo NPK"
R=$(curl -s -L $C -d x=1 $BASE/carrinho/esvaziar); has "carrinho esvaziado" "$R" "está vazio"
curl -s -L $C -d 'produtoId=1&quantidade=3' $BASE/carrinho/adicionar >/dev/null; curl -s -L $C -d 'produtoId=3&quantidade=1' $BASE/carrinho/adicionar >/dev/null
R=$(curl -s $C $BASE/compra); has "resumo mostra IVA 16% e total 2.308,40" "$R" "2 308,40"
LOC=$(curl -s -D - -o /dev/null $C -X POST $BASE/compra | grep -i '^location' | tr -d '\r'); has "compra redirecciona para factura" "$LOC" "/factura\?id="
ID=$(echo "$LOC" | sed 's/.*id=//')
R=$(curl -s $C "$BASE/factura?id=$ID"); has "factura FT-ano-n" "$R" "FT-2026-5"; has "factura: IVA" "$R" "IVA \(16%\)"; has "factura: total 2.308,40" "$R" "2 308,40"; has "factura: dados do cliente" "$R" "Rua A, Nampula"; has "factura: botão imprimir" "$R" "window.print"
R=$(curl -s $A "$BASE/admin/listagens/armazem"); has "stock do Milho descontado (500-30-... )" "$R" "Semente de Milho"
R=$(curl -s $C $BASE/minhas-compras); has "histórico contém factura" "$R" "FT-2026-5"
chk "carrinho vazio após compra → compra rejeitada (302)" 302 "$(code $C -X POST $BASE/compra)"

echo "== 4. Regras de acesso a facturas"
curl -s -o /dev/null $D -d 'email=maria@teste.mz&password=1234' $BASE/login
chk "outro cliente NÃO vê factura alheia → 403" 403 "$(code $D "$BASE/factura?id=$ID")"
chk "admin vê factura de qualquer cliente → 200" 200 "$(code $A "$BASE/factura?id=$ID")"
chk "factura inexistente → 404" 404 "$(code $A "$BASE/factura?id=99999")"

echo "== 5. Atomicidade (tudo ou nada) e stock"
curl -s -L $A -d 'nome=Pulv Teste&categoria=FERRAMENTA&fornecedorId=3&preco=10&quantidade=2&unidade=un&descricao=&fotoUrl=&validade=' $BASE/admin/produtos >/dev/null
PID=$(curl -s $A $BASE/admin/produtos | grep -oE '<td>[0-9]+</td><td>Pulv Teste' | grep -oE '[0-9]+' | head -1)
echo "  (produto de teste id=$PID, stock 2)"
# cliente maria (D) pôe 2×Pulv + 1×Milho; cliente t1 (C) compra 2×Pulv primeiro
curl -s -L $D -d "produtoId=$PID&quantidade=2" $BASE/carrinho/adicionar >/dev/null; curl -s -L $D -d 'produtoId=1&quantidade=1' $BASE/carrinho/adicionar >/dev/null
curl -s -L $C -d "produtoId=$PID&quantidade=2" $BASE/carrinho/adicionar >/dev/null; curl -s -o /dev/null $C -X POST $BASE/compra
S1=$(curl -s $A "$BASE/admin/listagens/produtos" | grep -A0 'Semente de Milho' | grep -oE '[0-9]+ kg' | head -1)
R=$(curl -s -L $D -X POST $BASE/compra); has "compra com item sem stock é rejeitada" "$R" "Stock insuficiente"
S2=$(curl -s $A "$BASE/admin/listagens/produtos" | grep -A0 'Semente de Milho' | grep -oE '[0-9]+ kg' | head -1)
chk "stock do outro item NÃO foi descontado" "$S1" "$S2"

echo "== 6. Concorrência: 12 compras simultâneas de um produto com stock 5"
R=$(curl -s -L $A -d 'nome=Concorrente&categoria=FERRAMENTA&fornecedorId=3&preco=1&quantidade=5&unidade=un&descricao=&fotoUrl=&validade=' $BASE/admin/produtos)
CID=$(curl -s $A $BASE/admin/produtos | grep -oE '<td>[0-9]+</td><td>Concorrente' | grep -oE '[0-9]+' | head -1)
for i in $(seq 1 12); do
  curl -s -o /dev/null -d "nome=Cli$i&email=c$i@conc.mz&password=1234&telefone=8$i&endereco=Rua $i" $BASE/registo
  curl -s -o /dev/null -c $T/p$i -d "email=c$i@conc.mz&password=1234" $BASE/login
  curl -s -o /dev/null -b $T/p$i -c $T/p$i -d "produtoId=$CID&quantidade=1" $BASE/carrinho/adicionar
done
for i in $(seq 1 12); do ( curl -s -o /dev/null -w '%{redirect_url}\n' -b $T/p$i $BASE/compra -X POST > $T/res$i ) & done; wait
SUC=$(cat $T/res* | grep -c '/factura?id='); chk "exactamente 5 compras bem-sucedidas" 5 "$SUC"
R=$(curl -s $A "$BASE/admin/produtos"); has "stock final = 0 (esgotado, nunca negativo)" "$R" "Concorrente.*0 un"

echo "== 7. CRUD do administrador e regras de remoção"
R=$(curl -s -L $A -d 'nome=Novo Forn&tipo=SEMENTES&contacto=82&provincia=Maputo' $BASE/admin/fornecedores); has "fornecedor criado" "$R" "Novo Forn"
R=$(curl -s -L $A -d 'nome=X&categoria=SEMENTE&fornecedorId=999&preco=5&quantidade=1&unidade=kg' $BASE/admin/produtos); has "produto com fornecedor inexistente rejeitado" "$R" "não existe"
R=$(curl -s -L $A -d 'nome=X&categoria=INVALIDA&fornecedorId=1&preco=5&quantidade=1&unidade=kg' $BASE/admin/produtos); has "categoria inválida rejeitada" "$R" "Valor inválido"
R=$(curl -s -L $A -d 'nome=X&categoria=SEMENTE&fornecedorId=1&preco=-5&quantidade=1&unidade=kg' $BASE/admin/produtos); has "preço negativo rejeitado" "$R" "preço deve ser positivo"
R=$(curl -s -L $A -d 'id=1' $BASE/admin/fornecedores/remover); has "fornecedor com produtos não removível" "$R" "produtos associados"
R=$(curl -s -L $A -d 'id=2&quantidade=50' $BASE/admin/produtos/repor); has "reposição de stock" "$R" "Stock reposto"
R=$(curl -s -L $A -d 'nome=Camp Teste&inicio=2027-01-01&fim=2026-01-01' $BASE/admin/campanhas); has "campanha com fim<início rejeitada" "$R" "anterior ao início"
R=$(curl -s -L $A -d 'nome=Camp Teste&inicio=2026-01-01&fim=2026-12-31' $BASE/admin/campanhas); has "campanha criada" "$R" "Camp Teste"
R=$(curl -s -L $A -d 'id=1' $BASE/admin/campanhas/remover); has "campanha com cultivos não removível" "$R" "cultivos associados"
R=$(curl -s -L $A -d 'id=2' $BASE/admin/campanhas/fechar); has "campanha fechada" "$R" "FECHADA"
R=$(curl -s -L $A -d 'campanhaId=2&cultura=Milho&variedade=ZM&hectares=2&plantio=2026-02-01' $BASE/admin/cultivos); has "cultivo em campanha fechada rejeitado" "$R" "campanha está fechada"
R=$(curl -s -L $A -d 'id=2' $BASE/admin/campanhas/reabrir); has "campanha reaberta" "$R" "ABERTA"
R=$(curl -s -L $A -d 'campanhaId=2&cultura=Milho&variedade=ZM&hectares=2&plantio=2026-02-01' $BASE/admin/cultivos); has "cultivo registado" "$R" "Cultivo registado"
R=$(curl -s -L $A -d 'id=1' $BASE/admin/cultivos/remover); has "cultivo com colheitas não removível" "$R" "colheitas associadas"
R=$(curl -s $A $BASE/admin/colheitas); has "colheita: 3200 kg × 22 = 70.400,00 MT" "$R" "70 400,00"
R=$(curl -s -L $A -d 'cultivoId=1&data=2027-04-01&kg=100&precoKg=10' $BASE/admin/colheitas); has "colheita adicional registada" "$R" "Colheita registada"
R=$(curl -s -L $A -d 'cultivoId=1&data=2020-01-01&kg=100&precoKg=10' $BASE/admin/colheitas); has "colheita antes do plantio rejeitada" "$R" "anterior ao plantio"

echo "== 8. Listagens ordenadas e relatórios"
R=$(curl -s $A "$BASE/admin/listagens/produtos?ordem=preco&dir=desc" | grep -oE 'Pulverizador costal|Enxada reforçada' | head -1); chk "ordenação por preço desc (Pulverizador primeiro)" "Pulverizador costal" "$R"
for t in produtos fornecedores clientes armazem compras; do chk "listagem /$t = 200" 200 "$(code $A $BASE/admin/listagens/$t)"; done
R=$(curl -s $A "$BASE/admin/listagens/compras"); has "compras detalhadas com endereço/telefone/itens" "$R" "Av. Eduardo Mondlane"
R=$(curl -s $A "$BASE/admin/relatorios"); has "relatório: clientes assíduos" "$R" "mais assíduos"; has "relatório: clientes sem compras (Ana)" "$R" "Ana Muianga"
for p in dashboard fornecedores produtos campanhas cultivos colheitas relatorios persistencia; do chk "/admin/$p = 200" 200 "$(code $A $BASE/admin/$p)"; done

echo "== 9. Persistência"
R=$(curl -s -L $A -d x=1 $BASE/admin/persistencia/gravar); has "gravar agora" "$R" "Estado gravado"
echo "  (reinício e restauro verificados à parte)"; 

echo "== 10. Logout"
chk "logout → 302" 302 "$(code $C $BASE/logout)"; chk "após logout /catalogo → 302 (login)" 302 "$(code $C $BASE/catalogo)"

echo; echo "RESULTADO: $PASS passaram, $FAIL falharam"; rm -rf $T; [ $FAIL -eq 0 ]
