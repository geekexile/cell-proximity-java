package proximity;

import java.util.Random;

public class Toolset {
	
	public int [] randomSortArray(int [] input) {
		
		int size = input.length;
		int[] indices = new int[size]; 
		for (int i=0; i < size; i++) { 
			indices[i] = i;
		}
		
		Random random = new Random();
		for (int i=0; i < size; i++) {
			
			boolean unique = false;
			int randomNo = 0;
			while (!unique) {
				unique = true;
				randomNo = random.nextInt(size);
				for (int j=0; j < i; j++) {
					if (indices[j] == randomNo) {
						unique = false;
						break;
					}
				}
			} 
			
			indices[i] = randomNo; 
		}
		
		int [] result = new int[size];
		for (int i=0; i < size; i++)
			result[indices[i]] = input[i];
		
		return result;
	}
	
}
