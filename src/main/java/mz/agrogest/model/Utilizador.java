package mz.agrogest.model;

import java.io.Serializable;

/** Modelo POJO serializável: Utilizador. */
public class Utilizador implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String email;
    private String passwordHash;
    private String salt;
    private String telefone;
    private String endereco;
    private String perfil;

    public Utilizador() {}

    public Utilizador(int id, String nome, String email, String passwordHash, String salt, String telefone, String endereco, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.telefone = telefone;
        this.endereco = endereco;
        this.perfil = perfil;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }


    public boolean isAdmin() { return "ADMIN".equals(perfil); }

}
