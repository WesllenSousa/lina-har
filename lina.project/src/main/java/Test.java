

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wesllen Sousa
 */
public class Test {

    public static void main(String[] args) throws Exception {
        
        double y = 0.2;
        double lR = 0;
        for (int t = 0; t < 30; t++) {
            double r = Math.exp((y * -1) * t);
            System.out.println(t + ")" + r);
            if(lR < r) {
                System.out.println("s");
            } else {
                System.out.println("n");
            }
            lR = r;
        }
        
        System.out.println(System.currentTimeMillis() / 100);
    }
}
