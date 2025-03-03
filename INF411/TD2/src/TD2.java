
/* TD2. Fruits et tables de hachage
 * Ce fichier contient 7 classes:
 * 		- Row représente une ligne de fruits,
 * 		- CountConfigurationsNaive compte naïvement les configurations stables,
 * 		- Quadruple manipule des quadruplets,
 * 		- HashTable construit une table de hachage,
 * 		- CountConfigurationsHashTable compte les configurations stables en utilisant notre table de hachage,
 * 		- Triple manipule des triplets,
 * 		- CountConfigurationsHashMap compte les configurations stables en utilisant la HashMap de java.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

class Row { // représente une ligne de fruits
	private final int[] fruits;

	// constructeur d'une ligne vide
	Row() {
		this.fruits = new int[0];
	}

	// constructeur à partir du champ fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// méthode equals pour comparer la ligne à un objet o
	@Override
	public boolean equals(Object o) {
		// on commence par transformer l'objet o en un objet de la classe Row
		// ici on suppose que o sera toujours de la classe Row
		Row that = (Row) o;
		// on vérifie que les deux lignes ont la meme longueur
		if (this.fruits.length != that.fruits.length)
			return false;
		// on vérifie que les ièmes fruits des deux lignes coincident
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// on a alors bien l'égalité des lignes
		return true;
	}

	// code de hachage de la ligne
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// chaîne de caracteres représentant la ligne
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	// Question 1

	// renvoie une nouvelle ligne en ajoutant fruit à la fin de la ligne
	Row addFruit(int fruit) {
		int[] tab=new int[this.fruits.length +1];
		for(int i=0;i<this.fruits.length;i++) {
			tab[i]=this.fruits[i];
		}
		
		tab[this.fruits.length]=fruit;
		return new Row(tab);
	}

	// renvoie la liste de toutes les lignes stables de largeur width
	static LinkedList<Row> allStableRows(int width) {
		
		LinkedList<Row> p= new LinkedList<>();
	    if(width==0) {
	    	p.add(new Row());
	    }
	    else {
	    	for(Row r:allStableRows(width-1)) {
				
				for(int i=0;i<2;i++) {
					int k=r.fruits.length;
					if(k<2||r.fruits[k-2]!=i||r.fruits[k-1]!=i) {
						p.add(r.addFruit(i));
					}
				}
			}
	    }
	    return p;
		
		
		
	
	
	
	

	}

	// vérifie si la ligne peut s'empiler avec les lignes r1 et r2
	// sans avoir trois fruits du même type adjacents
	boolean areStackable(Row r1, Row r2) {
		
		if(!(r1.fruits.length==r2.fruits.length)||!(r1.fruits.length==this.fruits.length))
			return false;
		for(int i=0;i<this.fruits.length;i++) {
			if(this.fruits[i]==r1.fruits[i]&&this.fruits[i]==r2.fruits[i]) {
				return false;
			}
		}
		return true;
	}
}

// COMPTAGE NAIF
class CountConfigurationsNaive { // comptage des configurations stables

	// Question 2

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		
		if(height<=1)
			return 0;
		if(height==2)
			return 1;
		else {
			long y=0;
			for(Row r:rows) {
				if(r.areStackable(r1, r2))
					y+=count(r2, r, rows, height-1);
			}
			return y;
		}
		

	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> l=Row.allStableRows(n);
		if(n<=1)
			return l.size();
		//Parcours des paires
		int c=0;
		
		for(Row r1:l) {
			for(Row r2:l) {
				c+=count(r1, r2, l, n);
			}
		}
		return c;
	}
}

// CONSTRUCTION ET UTILISATION D'UNE TABLE DE HACHAGE

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // table de hachage
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructeur
	HashTable() {
		Vector<LinkedList<Quadruple>> o=new Vector<>();
		for(int i=0;i<M;i++) {
			o.add(new LinkedList<Quadruple>());
		}
		this.buckets=o;
	}

	// Question 3.2

	// renvoie le code de hachage du triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		int h=height;
		for(int i=0;i<2;i++) {
			h+=(r1.hashCode()*r1.hashCode()+r2.hashCode());
		}
		return h;
	}

	// renvoie le seau du triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		return hashCode(r1,r2,height)%M;
	}

	// Question 3.3

	// ajoute le quadruplet (r1, r2, height, result) dans le seau indiqué par la
	// methode bucket
	void add(Row r1, Row r2, int height, long result) {
		int k=bucket(r1, r2, height);
		Quadruple q=new Quadruple(r1, r2, height, result);
		this.buckets.get(k).add(q);
	}

	// Question 3.4

	// recherche dans la table une entrée pour le triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		int k=bucket(r1, r2, height);
		for(Quadruple q:this.buckets.get(k)) {
			if(r1.equals(q.r1)&&r2.equals(q.r2)&& (height==q.height))
				return Long.valueOf(q.result);
		}
		return null;
	}

}

class CountConfigurationsHashTable { // comptage des configurations stables en utilisant notre table de hachage
	static HashTable memo = new HashTable();

	// Question 4

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant notre table de hachage
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if(height<=1)
			return 0;
		if(height==2)
			return 1;
		else {
			Long res=memo.find(r1, r2, height);
			if(res!=null)
				return res;
			res=(long)0;
			for(Row r:rows) {
				if(r.areStackable(r1, r2))
					res+=count(r2, r, rows, height-1);
			}
			memo.add(r1, r2, height, res);
			return res;
						
		}
	}

	// renvoie le nombre de grilles a n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> rows=Row.allStableRows(n);
		if(n<=1)
			return rows.size();
		long l =(long)0;
		for(Row r1:rows) {
			for(Row r2:rows)
				l+=count(r1, r2, rows, n);
			
		}
		return l;
	}
}

//UTILISATION DE HASHMAP

class Triple { // triplet (r1, r2, height)
	// à compléter
}

class CountConfigurationsHashMap { // comptage des configurations stables en utilisant la HashMap de java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant la HashMap de java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		throw new Error(
				"Méthode count(Row r1, Row r2, LinkedList<Row> rows, int height) de la classe CountConfigurationsHashMap à compléter (Question 5)");
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		throw new Error("Méthode count(int n) de la classe CountConfigurationsHashMap à compléter (Question 5)");
	}
}
