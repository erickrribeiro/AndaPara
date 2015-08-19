package com.promobile.epilesia.util;

public class FuncoesMATLAB {

    /**
     * Esta função é implementação do reshae do MATLAB que tem como objetivo modificar a dimensionalidade
     * de uma matriz.A modificação incluida, foi mudar a dimencionalidade de uma matriz linha para
     * a um matriz MxN onde M e N são passados por parâmetro.
     * @param A
     * @param m
     * @param n
     * @return
     */
	public static double[][] reshapeModificado(double[]A, int m, int n) {
        int origM = A.length;
        int origN = 1;
        
        if(origM*origN != m*n){
            throw new IllegalArgumentException("New matrix must be of same area as matix A");
        }
        double[][] B = new double[m][n];

        int index = 0;
        
        for(int i = 0;i< n;i++){
            for(int j = 0;j<m;j++){
                B[j][i] = A[index++];
            }

        }

        return B;
    }
	
	public static double[][] reshape(double[][] A, int m, int n) {
        int origM = A.length;
        int origN = A[0].length;
        if(origM*origN != m*n){
            throw new IllegalArgumentException("New matrix must be of same area as matix A");
        }
        double[][] B = new double[m][n];
        double[] A1D = new double[A.length * A[0].length];

        int index = 0;
        for(int i = 0;i<A.length;i++){
            for(int j = 0;j<A[0].length;j++){
                A1D[index++] = A[i][j];
            }
        }

        index = 0;
        for(int i = 0;i<n;i++){
            for(int j = 0;j<m;j++){
                B[j][i] = A1D[index++];
            }

        }
        return B;
    }
	public static double[] mean(double matriz[][]){		
		double columnsMeans[] = new double[matriz[0].length];
		
		for(int l= 0; l< matriz.length; l++)
		{		 
		    for(int c= 0; c < matriz[0].length; c++)
		    {
		    	columnsMeans[c] += matriz[l][c] / matriz.length;
		    }		    
		}
		
		return columnsMeans;	  
	}
	
	public static void show(Object matriz[][]){
		for(int l= 0; l< matriz.length; l++)
		{
		    for(int c= 0; c < matriz[0].length; c++)
		    {
		    	System.out.print(matriz[l][c]+"\t");		    	
		    }
		    System.out.println(" ");
		}
	}
	public static void show(double array[]){
		for(int l= 0; l< array.length; l++)
		{		    
		    System.out.println(array[l]);		    			    
		}
	}

	public static void show(int[] array) {
		for(int l= 0; l< array.length; l++)
		{		    
		    System.out.print(array[l]+" ");		    			    
		}
		
	}
}
