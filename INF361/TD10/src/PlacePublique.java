import java.security.MessageDigest;

import tc.TC;
public class PlacePublique extends Client {

	public PlacePublique(String pseudo) {
		super(pseudo);
		this.afficher(this.getPseudo() + ":");
	}

	public PlacePublique(String pseudo, Dialogue d) {
		super(pseudo, d);
		this.afficher(this.getPseudo() + ":");
	}

	@Override
	public void recevoirDuCanal(String paquet) {
		Paquet r=new Paquet(paquet);
		String h=r.getDestinataire();
		String o=this.getPseudo();
		if(r.getType()!=TypePaquet.__WRONG__&&(h.equals("__ALL__")||h.equals(o))) {
			this.afficher(paquet);
		}
		
	}

	@Override
	public void recevoirDuClavier(String ligne) {
		String s;
		if(ligne.equals("Hello")) {
			s=this.getPseudo()+";__ALL__;__HELLO__";
			
		}
		else {
			s=this.getPseudo()+";__ALL__;__MESSAGE__;0;0;"+ligne;
			
		}
		this.envoyer(s);
	}

	public static void main(String[] args) {
		new Canal_361(new PlacePublique("Momo")).lancer();
		// new Canal_361(new SimpleClient("Piccolo", new Dialogue(400, 0))).lancer();
	}
	
}
