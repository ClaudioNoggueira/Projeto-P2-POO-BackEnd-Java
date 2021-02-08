package model.entities;

import java.io.Serializable;

public class Contato implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String telefoneFixo;
	private String telefoneCelular;

	public Contato(Integer id, String email, String telefoneFixo, String telefoneCelular) {
		this.id = id;
		this.email = email;
		this.telefoneFixo = telefoneFixo;
		this.telefoneCelular = telefoneCelular;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefoneFixo() {
		return telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	@Override
	public String toString() {
		return "Contato [id=" + id + ", email=" + email + ", telefoneFixo=" + telefoneFixo + ", telefoneCelular="
				+ telefoneCelular + "]";
	}
}
