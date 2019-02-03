
/* C1769331
*
* Optionally, if you have any comments regarding your submission, put them here. 
* For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
*/

import java.util.*;
import java.io.*;

// A class that represents a dense vector and allows you to read/write its elements
class DenseVector {
	private int[] elements;
	
	public DenseVector(int n) {
		elements = new int[n];
	}
	
	public DenseVector(String filename) {
		File file = new File(filename);
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		try {
			Scanner sc = new Scanner(file);
			
			while (sc.hasNextInt()) {
				values.add(sc.nextInt());
			}
			
			sc.close();
			
			elements = new int[values.size()];
			for (int i = 0; i < values.size(); ++i) {
				elements[i] = values.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Read an element of the vector
	public int getElement(int idx) {
		return elements[idx];
	}
	
	// Modify an element of the vector
	public void setElement(int idx, int value) {
		elements[idx] = value;
	}
	
	// Return the number of elements
	public int size() {
		return (elements == null) ? 0 : (elements.length);
	}
	
	// Print all the elements
	public void print() {
		if (elements == null) {
			return;
		}
		
		for (int i = 0; i < elements.length; ++i) {
			System.out.println(elements[i]);
		}
	}
}

// A class that represents a sparse matrix
public class SparseMatrix {
	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a matrix and print information: java SparseMatrix -i <MatrixFile>");
		System.err.println(" - Read a matrix and print elements: java SparseMatrix -r <MatrixFile>");
		System.err.println(" - Transpose a matrix: java SparseMatrix -t <MatrixFile>");
		System.err.println(" - Add two matrices: java SparseMatrix -a <MatrixFile1> <MatrixFile2>");
		System.err.println(" - Matrix-vector multiplication: java SparseMatrix -v <MatrixFile> <VectorFile>");
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			printCommandError();
			System.exit(-1);
		}
		
		if (args[0].equals("-i")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}
			
			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			System.out.println("The matrix has " + mat.getNumRows() + " rows and " + mat.getNumColumns() + " columns");
			System.out.println("It has " + mat.numNonZeros() + " non-zeros");
		} else if (args[0].equals("-r")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}
			
			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
		} else if (args[0].equals("-t")) {
			if (args.length != 2) {
				printCommandError();
				System.exit(-1);
			}
			
			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			System.out.println("Read matrix from " + args[1]);
			SparseMatrix transpose_mat = mat.transpose();
			System.out.println();
			System.out.println("Matrix elements:");
			mat.print();
			System.out.println();
			System.out.println("Transposed matrix elements:");
			transpose_mat.print();
		} else if (args[0].equals("-a")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}
			
			SparseMatrix mat1 = new SparseMatrix();
			mat1.loadEntries(args[1]);
			System.out.println("Read matrix 1 from " + args[1]);
			System.out.println("Matrix elements:");
			mat1.print();
			
			System.out.println();
			SparseMatrix mat2 = new SparseMatrix();
			mat2.loadEntries(args[2]);
			System.out.println("Read matrix 2 from " + args[2]);
			System.out.println("Matrix elements:");
			mat2.print();
			SparseMatrix mat_sum1 = mat1.add(mat2);
			
			System.out.println();
			mat1.multiplyBy(2);
			SparseMatrix mat_sum2 = mat1.add(mat2);
			
			mat1.multiplyBy(5);
			SparseMatrix mat_sum3 = mat1.add(mat2);
			
			System.out.println("Matrix1 + Matrix2 =");
			mat_sum1.print();
			System.out.println();
			
			System.out.println("Matrix1 * 2 + Matrix2 =");
			mat_sum2.print();
			System.out.println();
			
			System.out.println("Matrix1 * 10 + Matrix2 =");
			mat_sum3.print();
		} else if (args[0].equals("-v")) {
			if (args.length != 3) {
				printCommandError();
				System.exit(-1);
			}
			
			SparseMatrix mat = new SparseMatrix();
			mat.loadEntries(args[1]);
			DenseVector vec = new DenseVector(args[2]);
			DenseVector mv = mat.multiply(vec);
			
			System.out.println("Read matrix from " + args[1] + ":");
			mat.print();
			System.out.println();
			
			System.out.println("Read vector from " + args[2] + ":");
			vec.print();
			System.out.println();
			
			System.out.println("Matrix-vector multiplication:");
			mv.print();
		}
	}
	
	private ArrayList<Entry> mergesort(ArrayList<Entry> D){
		if (D.size() == 1) {
			return D;
		}
		int mid = Math.floorDiv(D.size(), 2);
		ArrayList<Entry> A = new ArrayList<Entry>(D.subList(0, mid));
		ArrayList<Entry> B = new ArrayList<Entry>(D.subList(mid, D.size()));
		A = mergesort(A);
		B = mergesort(B);
		return merge(A, B);
	}

	
	private ArrayList<Entry> merge(ArrayList<Entry> A, ArrayList<Entry> B){
		int i = 0;
		int j = 0;
		int k = 0;
		ArrayList<Entry> C = new ArrayList<Entry>();
		for (int x = 0; x < (A.size() + B.size()); x++) {
			C.add(null);
		}
		while (i < A.size() && j < B.size()){
			if (A.get(i).getPosition() < B.get(j).getPosition()){
				C.set(k, A.get(i));
				i++; k++;
			} else {
				C.set(k, B.get(j));
				j++; k++;
			}
		}
		while (i < A.size()) {
			C.set(k, A.get(i));
			i++; k++;
		}
		while (j < B.size()) {
			C.set(k, B.get(j));
			j++; k++;
		}
		return C;
	}	

	// Loading matrix entries from a text file
	// You need to complete this implementation
	public void loadEntries(String filename) {
		File file = new File(filename);
		
		try {
			Scanner sc = new Scanner(file);
			numRows = sc.nextInt();
			numCols = sc.nextInt();
			entries = new ArrayList<Entry>();
			
			while (sc.hasNextInt()) {
				// Read the row index, column index, and value of an element
				int row = sc.nextInt();
				int col = sc.nextInt();
				int val = sc.nextInt();
				
				// Add your code here to add the element into data member entries
				entries.add(new Entry(col + row * numCols, val));
			}
			
			// Add your code here for sorting non-zero elements

			entries = mergesort(entries);
			
		} catch (Exception e) {
			e.printStackTrace();
			numRows = 0;
			numCols = 0;
			entries = null;
		}
	}
	
	// Default constructor
	public SparseMatrix() {
		numRows = 0;
		numCols = 0;
		entries = null;
	}
	
	// A class representing a pair of column index and elements
	private class Entry {
		private int position; // Position within row-major full array representation
		private int value; // Element value
		
		// Constructor using the column index and the element value
		public Entry(int pos, int val) {
			this.position = pos;
			this.value = val;
		}
		
		// Copy constructor
		public Entry(Entry entry) {
			this(entry.position, entry.value);
		}
		
		// Read column index
		int getPosition() {
			return position;
		}
		
		// Set column index
		void setPosition(int pos) {
			this.position = pos;
		}
		
		// Read element value
		int getValue() {
			return value;
		}
		
		// Set element value
		void setValue(int val) {
			this.value = val;
		}
	}
	
	// Adding two matrices
	public SparseMatrix add(SparseMatrix M) {
		SparseMatrix total = new SparseMatrix();
		total.entries = new ArrayList<Entry>();
		total.numCols = numCols;
		total.numRows = numRows;

		// Shallow copy lists to not affect the originals
		ArrayList<Entry> a = new ArrayList<Entry>(entries);
		ArrayList<Entry> b = new ArrayList<Entry>(M.entries);

		while (a.size() > 0 && b.size() > 0) {
			if (a.get(0).getPosition() == b.get(0).getPosition()) {
				Entry e = new Entry(a.get(0).getPosition(), a.get(0).getValue() + b.get(0).getValue());
				total.entries.add(e);
				a.remove(0); b.remove(0);
			} else if (a.get(0).getPosition() < b.get(0).getPosition()){
				Entry e = new Entry(a.get(0).getPosition(), a.get(0).getValue());
				total.entries.add(e);
				a.remove(0);
			} else {
				Entry e = new Entry(b.get(0).getPosition(), b.get(0).getValue());
				total.entries.add(e);
				b.remove(0);
			}
		}

		while (a.size() > 0){
			Entry e = new Entry(a.get(0).getPosition(), a.get(0).getValue());
			total.entries.add(e);
			a.remove(0);
		}

		while (b.size() > 0){
			Entry e = new Entry(b.get(0).getPosition(), b.get(0).getValue());
			total.entries.add(e);
			b.remove(0);
		}

		total.entries = mergesort(total.entries);
		return total;
	}

	// Transposing a matrix
	public SparseMatrix transpose() {
		SparseMatrix t = new SparseMatrix();
		t.numCols = numRows;
		t.numRows = numCols;
		t.entries = new ArrayList<Entry>();
		for (Entry e : entries) {
			int x = e.getPosition() % numCols;
			int y = e.getPosition() / numCols;
			t.entries.add(new Entry(y + x * t.numCols, e.getValue()));
		}
		t.entries = mergesort(t.entries);
		return t;
	}
	
	// Matrix-vector multiplication
	public DenseVector multiply(DenseVector v) {
		// Add your code here
		return null;
	}
	
	// Return the number of non-zeros
	public int numNonZeros() {
		return entries.size();
	}
	
	// Multiply the matrix by a scalar, and update the matrix elements
	public void multiplyBy(int scalar) {
		for (Entry e : entries) {
			e.setValue(e.getValue() * scalar);
		}
	}
	
	// Number of rows of the matrix
	public int getNumRows() {
		return this.numRows;
	}
	
	// Number of columns of the matrix
	public int getNumColumns() {
		return this.numCols;
	}
	
	// Output the elements of the matrix, including the zeros
	// Do not modify this method
	public void print() {
		int n_elem = numRows * numCols;
		int pos = 0;
		
		for (int i = 0; i < entries.size(); ++i) {
			int nonzero_pos = entries.get(i).getPosition();
			
			while (pos <= nonzero_pos) {
				if (pos < nonzero_pos) {
					System.out.print("0 ");
				} else {
					System.out.print(entries.get(i).getValue());
					System.out.print(" ");
				}
				
				if ((pos + 1) % this.numCols == 0) {
					System.out.println();
				}
				
				pos++;
			}
		}
		
		while (pos < n_elem) {
			System.out.print("0 ");
			if ((pos + 1) % this.numCols == 0) {
				System.out.println();
			}
			
			pos++;
		}
	}
	
	private int numRows; // Number of rows
	private int numCols; // Number of columns
	private ArrayList<Entry> entries; // Non-zero elements
}
