package br.com.rpg.model;

public class Atributos {
    private int vidaMaxima;
    private int vidaAtual;
    private int ataque;
    private int defesa;
    private int sorte;
    private double esquivaBase;
    private double chanceCriticoBase;
    private int nivel;
    private int experiencia;
    private int experienciaProximoNivel;
    private int ouro;

    public Atributos(int vidaMaxima, int ataque, int defesa, int sorte, double esquivaBase, double chanceCriticoBase) {
        this.vidaMaxima = Math.max(1, vidaMaxima);
        this.vidaAtual = this.vidaMaxima;
        this.ataque = Math.max(1, ataque);
        this.defesa = Math.max(0, defesa);
        this.sorte = Math.max(1, sorte);
        this.esquivaBase = Math.max(0.0, esquivaBase);
        this.chanceCriticoBase = Math.max(0.0, chanceCriticoBase);
        this.nivel = 1;
        this.experiencia = 0;
        this.experienciaProximoNivel = 100;
        this.ouro = 40;
    }

    public int receberDanoBruto(int danoBruto, int defesaEfetiva) {
        int mitigacao = (int) Math.round(defesaEfetiva * 0.55);
        int danoReal = Math.max(1, danoBruto - mitigacao);
        this.vidaAtual = Math.max(0, this.vidaAtual - danoReal);
        return danoReal;
    }

    public void sofrerDanoDireto(int dano) {
        this.vidaAtual = Math.max(0, this.vidaAtual - Math.max(0, dano));
    }

    public void sofrerDanoNaoFatal(int dano) {
        int danoAplicado = Math.min(Math.max(0, dano), Math.max(0, this.vidaAtual - 1));
        this.vidaAtual -= danoAplicado;
    }

    public int curar(int quantidade) {
        if (quantidade <= 0) {
            return 0;
        }
        int vidaAntes = this.vidaAtual;
        this.vidaAtual = Math.min(this.vidaMaxima, this.vidaAtual + quantidade);
        return this.vidaAtual - vidaAntes;
    }

    public boolean ganharExperiencia(int xpGanho) {
        if (xpGanho <= 0) {
            return false;
        }
        this.experiencia += xpGanho;
        boolean subiuNivel = false;
        while (this.experiencia >= this.experienciaProximoNivel) {
            this.experiencia -= this.experienciaProximoNivel;
            subirNivel();
            subiuNivel = true;
        }
        return subiuNivel;
    }

    private void subirNivel() {
        this.nivel++;
        this.experienciaProximoNivel = (int) Math.round(this.experienciaProximoNivel * 1.35);
        this.vidaMaxima += 18;
        this.vidaAtual = this.vidaMaxima;
        this.ataque += 4;
        this.defesa += 2;
        this.sorte += 2;
        this.esquivaBase = Math.min(45.0, this.esquivaBase + 0.5);
    }

    public boolean estaVivo() {
        return this.vidaAtual > 0;
    }

    public void adicionarOuro(int valor) {
        this.ouro = Math.max(0, this.ouro + valor);
    }

    public boolean gastarOuro(int valor) {
        if (valor < 0 || this.ouro < valor) {
            return false;
        }
        this.ouro -= valor;
        return true;
    }

    public void aumentarSorte(int delta) {
        this.sorte = Math.max(1, this.sorte + delta);
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = Math.max(1, vidaMaxima);
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public void setVidaAtual(int vidaAtual) {
        this.vidaAtual = Math.max(0, Math.min(vidaMaxima, vidaAtual));
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = Math.max(1, ataque);
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) {
        this.defesa = Math.max(0, defesa);
    }

    public int getSorte() {
        return sorte;
    }

    public void setSorte(int sorte) {
        this.sorte = Math.max(1, sorte);
    }

    public double getEsquivaBase() {
        return esquivaBase;
    }

    public void setEsquivaBase(double esquivaBase) {
        this.esquivaBase = Math.max(0.0, esquivaBase);
    }

    public double getChanceCriticoBase() {
        return chanceCriticoBase;
    }

    public void setChanceCriticoBase(double chanceCriticoBase) {
        this.chanceCriticoBase = Math.max(0.0, chanceCriticoBase);
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = Math.max(1, nivel);
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = Math.max(0, experiencia);
    }

    public int getExperienciaProximoNivel() {
        return experienciaProximoNivel;
    }

    public void setExperienciaProximoNivel(int experienciaProximoNivel) {
        this.experienciaProximoNivel = Math.max(10, experienciaProximoNivel);
    }

    public int getOuro() {
        return ouro;
    }

    public void setOuro(int ouro) {
        this.ouro = Math.max(0, ouro);
    }
}
