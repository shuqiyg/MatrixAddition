/********************************************** 
Workshop # 9
Course: JAC433
Last Name:Yang
First Name:Shuqi
ID:132162207
Section:NBB 
This assignment represents my own work in accordance with Seneca Academic Policy. 
Signature 
Date:2022-04-06
**********************************************/ 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixAddition {
	static long timeParallel, timeSequential;
	 
	public static void main(String[] args) {
		double[][] matrixA = generateMatrix(2000,2000);
		double[][] matrixB = generateMatrix(2000,2000);
		double[][] resultPara = parallelAddMatrix(matrixA, matrixB);
		double[][] resultSeq = sequentialAddMatrix(matrixA, matrixB);
		System.out.println("\n*** Check if the addition results from Parallel version and Sequential match ***");
		System.out.println("resultPara[0][0]: " + resultPara[0][0] + "  resultSeq[0][0]: " + resultSeq[0][0] + "\nresultPara[1999][1999]: " + resultPara[1999][1999] + "  resultSeq[1999][1999]: " + resultSeq[1999][1999]);
		String str = "Hello you little: big tities!";
		String sub_str = str.substring(0, str.indexOf(":")+1);
		System.out.println(sub_str);
		System.out.println(sub_str.replace("He", "HHHEe"));
		
	}
	
	public static double[][] generateMatrix(int row, int column){
		double[][] matrix = new double[row][column];
		for(int i = 0; i<matrix.length; i++) {
			for(int j = 0; j<matrix[i].length;j++) {
				matrix[i][j] = (int)(Math.random()*10 + 1);
			}
		}	
		return matrix;
	};
	
	public static double[][] parallelAddMatrix(double[][]a, double[][]b){
		//divide a, b into four
		double [][] newMatrix = new double[a.length][a[0].length];
		
		double [][] subMatrixA1 = new double[a.length/4][a[0].length];
		double [][] subMatrixA2 = new double[a.length/4][a[0].length];
		double [][] subMatrixA3 = new double[a.length/4][a[0].length];
		double [][] subMatrixA4 = new double[a.length/4][a[0].length]; 
		
		System.arraycopy(a, 0, subMatrixA1, 0, a.length/4);
		System.arraycopy(a, (int)((double)1/4 * a.length), subMatrixA2, 0, a.length/4);
		System.arraycopy(a, (int)((double)2/4 * a.length), subMatrixA3, 0, a.length/4);
		System.arraycopy(a, (int)((double)3/4 * a.length), subMatrixA4, 0, a.length/4);
		
		double [][] subMatrixB1 = new double[a.length/4][a[0].length/4];
		double [][] subMatrixB2 = new double[a.length/4][a[0].length/4];
		double [][] subMatrixB3 = new double[a.length/4][a[0].length/4];
		double [][] subMatrixB4 = new double[a.length/4][a[0].length/4];
		
		System.arraycopy(b, 0, subMatrixB1, 0, a.length/4);
		System.arraycopy(b, (int)((double)1/4 * a.length), subMatrixB2, 0, a.length/4);
		System.arraycopy(b, (int)((double)2/4 * a.length), subMatrixB3, 0, a.length/4);
		System.arraycopy(b, (int)((double)3/4 * a.length), subMatrixB4, 0, a.length/4);
		
		Thread addOne = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < (int)((double)1/4 * a.length); i++) {
					for(int j = 0; j < newMatrix[i].length; j++) {
						newMatrix[i][j] = subMatrixA1[i][j] + subMatrixB1[i][j];
					}
				}
			}	
		});
		Thread addTwo = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = (int)((double)1/4 * a.length), k = 0; i < (int)((double)2/4 * a.length) && k <a.length/4; i++, k++) {
					for(int j = 0; j < newMatrix[i].length; j++) {
						//pay attention to the index of submatrix because it uses different starting index point than the new Matrix // we record the new elements from where we left off from the last additiion
						newMatrix[i][j] = subMatrixA2[k][j] + subMatrixB2[k][j];
					}
				}
			}	
		});
		Thread addThree = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = (int)((double)2/4 * a.length), k = 0; i < (int)((double)3/4 * a.length) && k < a.length/4; i++, k++) {
					for(int j = 0; j < newMatrix[i].length; j++) {
						newMatrix[i][j] = subMatrixA3[k][j] + subMatrixB3[k][j];
					}
				}
			}	
		});
		Thread addFour = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = (int)((double)3/4 * a.length), k = 0; i < a.length && k < a.length; i++, k++) {
					for(int j = 0; j < newMatrix[i].length; j++) {
						newMatrix[i][j] = subMatrixA4[k][j] + subMatrixB4[k][j];
					}
				}
			}	
		});
		
		final long startTime = System.currentTimeMillis();
		//ExecutorService executor = Executors.newCachedThreadPool();
		addOne.start();
		addTwo.start();
		addThree.start();
		addFour.start();
		final long endTime = System.currentTimeMillis();
		System.out.println("Parallel Methold Execution Time in Milliseconds: " + (endTime - startTime));
		System.out.println();
		timeParallel = endTime - startTime;

		return newMatrix;
		
	}
	public static double[][] sequentialAddMatrix(double[][]a, double[][]b){
		final long startTime = System.currentTimeMillis();
		int row = a.length, column = a[0].length;
		double[][] newMatrix = new double[row][column];
		for(int i = 0; i< row; i++) {
			for(int j = 0; j < column; j++) {
				newMatrix[i][j] = a[i][j] + b[i][j];
			}
		}
		final long endTime = System.currentTimeMillis();
		System.out.println("Sequential Methold Execution Time in Milliseconds: " + (endTime - startTime));
		System.out.println();
		timeSequential = endTime - startTime;
		return newMatrix;
		
	}
}
