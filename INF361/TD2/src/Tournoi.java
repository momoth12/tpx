import tc.TC;
public class Tournoi {
	
	public static void afficheTour(int[] tab) {
		int i;
		int n=tab.length;
		for(i=0;i<n;i=i+2) {
			if(i<n-2) {
				TC.print("["+tab[i]+" - "+tab[i+1]+"]");
				TC.print(" ");
			}
			else
				TC.print("["+tab[i]+" - "+tab[i+1]+"]");
				
			
		}
		TC.println();	
		
		
		
	}

	
	public static int[] tableauOrdonne(int n) {
		int[] tab=new int[n];
		int i;
		for(i=0;i<n;i++)
			tab[i]=i+1;
		return(tab);
		
		
	}
}
