public class ExMapa03 extends BibliKarel {
    
    public void run() {
        setPosOrig(true);
        do {
            andarComDesvio(false);
        } while (!facingWest());
        retornarPosicao();
    }
    
    public void retornarPosicao() {
        turnLeft();
        while(andar());
        turnLeft();
    }
    
    public static void main(String [] args) {
        new ExMapa03();
    }
}