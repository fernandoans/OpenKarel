public class RodaMundo extends BibliKarel {
    
    public void run() {
        do {
            while (andar()) pegarSinalizador();
        } while(up());
        System.out.println("Terminei de subir...");
    }
        
    public static void main(String [] args) {
        new RodaMundo();
    }
    
}
