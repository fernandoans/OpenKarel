import openKarel.XKarel;

public abstract class BibliKarel extends XKarel {

    private boolean posOrig;
    
    public void setPosOrig(boolean posOrig) {
        this.posOrig = posOrig;
    }
    
    public boolean up() {
        if (facingEast()) {
            return exitLeft();
        } else if (facingWest()) {
            return exitRight();
        }
        return true;
    }
    
    public boolean exitLeft() {
        turnLeft();
        if (andar()) {
          pegarSinalizador();  
          turnLeft();
          return true;
        }
        return false;
    }
    
    public boolean exitRight() {
        turnRight();
        if (andar()) {
          pegarSinalizador();  
          turnRight();
          return true;
        }
        return false;
    }
    
    public void turnRight() {
        turnLeft();
        turnLeft();
        turnLeft();
    }
    
    public void pegarSinalizador() {
        if (beepersPresent()) {
            pickBeeper();
        }
    }
    
    public boolean andar() {
        if (frontIsClear()) {
            move();
            return true;
        }
        return false;
    }
    
    public void andarComDesvio(boolean quina) {
        if (!andar()) {
            pegarSinalizador();
            turnLeft();
        }
        if (rightIsClear()) {
            turnRight();
            if (quina) {
                andar();
            }
        }
    }
    
    public boolean volta() {
        if (posOrig && facingWest()) {
            posOrig = false;
        }
        if (!posOrig && facingEast()) {
            return false;
        }
        return true;
    } 
    
    public void deixarSinalizador() {
        if (beepersInBag()) {
            putBeeper();
        }
    }    
}
