package com.promobile.epilesia.HMM;
/*
* The Viterbi algorithm in Java
* Author: Paul Fodor <pfodor@cs.sunysb.edu>
* Stony Brook University, 2007
* Python version: http://en.wikipedia.org/wiki/Viterbi_algorithm
*/

public class Viterbi{
	public String[] estados;
	public String[] observacoes;
	public double[] probabilidadeInicial;
	public double[][] transicaoProbabilidade;
	public double[][] simbolosProbabilidade;
	
	public Viterbi(String[] estados, String[] observacoes, double[] probabilidadeInicial,
			double[][] transicaoProbabilidade, double[][] simbolosProbabilidade) {		
		this.estados = estados;
		this.observacoes = observacoes;
		this.probabilidadeInicial = probabilidadeInicial;
		this.transicaoProbabilidade = transicaoProbabilidade;
		this.simbolosProbabilidade = simbolosProbabilidade;		
	}

	public int simboloParaIndice(String simbolo){
		return Integer.valueOf(simbolo)-1;
	}
	
	public String getResultado(String[] path, String[] estados){
		int votos[] = new int[estados.length];
		
		System.out.println("");
		for(int i=0; i< path.length; i++){
			for(int j=0; j< estados.length; j++){				
				if(estados[j].equals(path[i])){
					votos[j] += 1;
					break;
				}
			}
		}
		
		String resultado = "";

		if(votos[0] == Math.max(votos[0], votos[1])){
			resultado = "Andando";
		}else{
			resultado = "Parado";
		}
		
		return resultado;
	}
	
	public String forwardViterbi(String[] observacoes) {
        this.observacoes = observacoes;

		double[][] A = this.transicaoProbabilidade;
		String[] O = this.observacoes;
		String[] S = this.estados;
		double[][] B = this.simbolosProbabilidade;
		double[] R = this.probabilidadeInicial;
		
		double [][] delta = new double[observacoes.length][estados.length];
		int[][] psi = new int[observacoes.length][estados.length];
		String[] path = new String[observacoes.length];
		/**
		 * Inicialização para o caso base (t=0)
		 */		
		
		int y = simboloParaIndice(O[0]);
		
		for(int i = 0; i<this.estados.length; i++){
			delta[0][i] = R[i] * B[i][y];
		}
		
		/**
		 * Passo Interativo, onde t > 0
		 */
	
		for(int t=1; t < this.observacoes.length; t++){			

			y = simboloParaIndice(O[t]);

			for (int j=0; j < this.estados.length; j++){
				//(prob, state) = max((V[t-1][y0] * trans_p[y0][y] * emit_p[y][obs[t]], y0) for y0 in states)
				
				/**
				 * max[δt(t-1)(i) * aij]
				 */
				double maior = -9999999;
				int indice = 0;
				
				for(int i=0; i < this.estados.length; i++){
					delta[t][j] = delta[t-1][i] * A[i][j];
					
					/**
					 * Calcular o max
					 */
					if(delta[t][j] > maior){
						maior = delta[t][j];
						indice = i;
					}					
				}
				
				/**
				 * max[δt(t-1)(i) * aij]*bj(Ot)
				 */
				delta[t][j] = maior * B[j][y];
											
				/**
				 * Ψt(j) = arg max[δt(t-1)(i) * aij]
				 */
				psi[t][j] = indice+1;
			}			
		}	

		/**
		 * Terminaçao 
		 * P* = max[δT(i)]
		 */
				
		int T = observacoes.length-1; //Menos um porque os arrays comecam de zero. 
		
		double maior = -9999999;
		int indice = 0;
		
		for(int i=0; i < estados.length; i++){				
			/**
			 * Calcular o max
			 */
			if(delta[T][i] > maior){
				maior = delta[T][i];
				indice = i;
			}
		}
		path[T] = ""+(indice+1);

		for(int t=T-1; t >= 0 ; t--){
			maior = -9999999;
			indice = 0;

			for(int i=0; i < estados.length; i++){				
				/**
				 * Calcular o max
				 */
				if(delta[t][i] > maior){
					maior = delta[t][i];
					indice = psi[t][i];
				}
			}
			path[t] = ""+(indice);
		}

		/*for(String v: path){
			System.out.print(" "+v);
		}*/
		
		String resultado = getResultado(path, this.estados);		
			
		return resultado; 
		
	}
}
