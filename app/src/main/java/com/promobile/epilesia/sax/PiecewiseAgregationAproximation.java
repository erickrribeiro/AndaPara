package com.promobile.epilesia.sax;

import com.promobile.epilesia.util.FuncoesMATLAB;
import com.promobile.epilesia.util.Statistics;

public class PiecewiseAgregationAproximation {
	private int numeroDeSegmentos;
	private int tamanhoDoAlfabeto;
	private int tamanhoDaJanela;
	private double serie[];
	
	
	public PiecewiseAgregationAproximation(int tamanhoDoSeguimento, int tamanhoDoAlfabeto, double[] serie) throws Exception{
		this.serie = serie; //serie.stream().mapToDouble(Double::doubleValue).toArray();
		this.tamanhoDoAlfabeto = tamanhoDoAlfabeto;
		this.numeroDeSegmentos = this.serie.length / tamanhoDoSeguimento;
		
		this.tamanhoDaJanela =  (int) this.serie.length/ this.numeroDeSegmentos;
		validação();
	}

    /**
     * Este método é responsável por validar os parametros passado para o construtor.
     * @throws Exception
     */
	private void validação() throws Exception{
		if(this.tamanhoDoAlfabeto > 10){
			throw new Exception("O tamanho do alfabeto não poder ser maior que 10.");
		}
		if ((this.serie.length % this.numeroDeSegmentos) > 0  ){
			throw new Exception("O numero de segmentos deve ser divisivel pela tamanho da series.");
		}
	}
	
	public double[] executar(){
		double paa[];
		
		normalizacaoZeroUm();

		/**
		 * Caso espercial, sem redução de remensionalidade.
		 */
		if(this.serie.length == this.numeroDeSegmentos){
			paa = this.serie;
		}else{
			paa = FuncoesMATLAB.mean(FuncoesMATLAB.reshapeModificado(this.serie, this.tamanhoDaJanela, this.numeroDeSegmentos));
		}
		
		return paa;
	}
	
	
	private void normalizacaoZeroUm(){
		int i;
		double desvioPradao;
		double media;

		Statistics statistics =  new Statistics(serie);
		desvioPradao = statistics.getStdDev();
		media =  statistics.getMean();
		
		for (i = 0; i < serie.length; i++){
		 	serie[i] = (serie[i] - media) / desvioPradao;
		}					
	}
	
	public int getTamanhoDoAlfabeto() {
		return tamanhoDoAlfabeto;
	}

}
