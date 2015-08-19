package com.promobile.epilesia.sax;

public class SymbolicAggregateApproXimation {
	private PiecewiseAgregationAproximation paa;	
	private double[] series;
	private int sax[]; 
	
	public SymbolicAggregateApproXimation(int numeroDeSegmentos, int tamanhoDoAlfabeto, double[] serie) throws Exception{
		this.paa = new PiecewiseAgregationAproximation(numeroDeSegmentos, tamanhoDoAlfabeto, serie);
		this.series = paa.executar();
		this.sax =  new int[33];
	}
	
	private int[] paaToSax(double linhaDeCorte []){
		int[] array=  new int[this.series.length];
		int cont = 1;
		for (int i=0; i< this.series.length; i++){
			for (int y=0; y< linhaDeCorte.length; y++){
				if(linhaDeCorte[y] <= this.series[i] ) {
					cont++;
				}
			}			
			System.out.print(cont+" ");
			array[i] = cont;
			cont = 1;			
		}
		return array;
	}
	
	public void executar(){		
		switch (this.paa.getTamanhoDoAlfabeto()){
			case 2:
				this.sax = paaToSax(new double[] {0});
			break;
			case 3:
				this.sax = paaToSax(new double[] {-0.43, 0.43});
			break;
			
			case 4:					
				this.sax = paaToSax(new double[] {-0.67, 0, 0.67});
			break;
			
			case 5:			
				this.sax = paaToSax(new double[] {-0.84, -0.25, 0.25, 0.84});
			break;
			
			case 6:
				this.sax = paaToSax(new double[] {-0.97, -0.43, 0, 0.43, 0.97});		
			break;
			
			case 7:
				this.sax = paaToSax(new double[] {-1.07, -0.57, -0.18, 0.18, 0.57, 1.07});			
			break;
			
			case 8:			
				this.sax = paaToSax(new double[] {-1.15, -0.67, -0.32, 0, 0.32, 0.67, 1.15});
			break;
			
			case 9:
				this.sax = paaToSax(new double[] {-1.22, -0.76, -0.43, -0.14, 0.14, 0.43, 0.76, 1.22});
			break;
			
			case 10:
				this.sax = paaToSax(new double[] {-1.28, -0.84, -0.52, -0.25, 0, 0.25, 0.52, 0.84, 1.28});			
			break;
			
			default:
				System.out.println("WARNING:: Alphabet size too big");
				break;
			}
	}
		
	public double[] getSeries() {
		return series;
	}
	
	public int[] getSax() {
		return sax;
	}
}
