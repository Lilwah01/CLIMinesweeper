
public class RandomTest {

	public static void main(String[] args) {
		
		int size = 3;
		int[] array = new int[size*size];
		int maxAmount = (size*size)-1;
		int targetAmount = 9;
		
		int[] results = new int[targetAmount];
		
		for(int i=0; i<array.length; i++) {
			array[i] = i;
		}
		
		for(int i=0; i<targetAmount; i++) {
			int key = (0+(int)(((maxAmount)-1)*(Math.random())));
			results[i] = array[key];
			swap(array, key, maxAmount);
			maxAmount-=1;
		}
		
		for(int i=0; i<results.length; i++) {
			System.out.println(results[i]);
			System.out.println();
			int column = results[i]/size;
			int row = results[i]%size;
			System.out.println(column+","+row);
		}

	}
	
	
	public static void swap(int[] array, int a, int b) {
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
}
