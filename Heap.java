/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;

/**
 *
 * @author atul
 */
public class Heap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        {
            PrimsAlgorithm p = new PrimsAlgorithm(args[0], "p_"+args[0]);
            p.printMST();
        }
        
        //{
          //  FredmanTarjanMST f = new FredmanTarjanMST(args[0], "f_"+args[0]);
            //f.printMST();
        //}
    }

}
