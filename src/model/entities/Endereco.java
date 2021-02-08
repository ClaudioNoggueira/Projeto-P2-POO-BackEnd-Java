package model.entities;

import java.io.Serializable;

import model.entities.enums.UnidadeFederativa;

public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String logradouro;
	private String bairro;
	private String cidade;
	private String cep;
	private Integer uf;

	public Endereco(Integer id, String logradouro, String bairro, String cidade, String cep, Integer uf) {
		this.id = id;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cidade = cidade;
		this.cep = cep;
		this.uf = uf;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public UnidadeFederativa getUf() {
		return UnidadeFederativa.valueOf(uf);
	}

	public void setUf(UnidadeFederativa uf) {
		if (uf != null) {
			this.uf = uf.getCodigo();
		}
	}

	@Override
	public String toString() {
		return "Endereco [id=" + id + ", logradouro=" + logradouro + ", bairro=" + bairro + ", cidade=" + cidade
				+ ", cep=" + cep + ", uf=" + uf + "]";
	}
}
