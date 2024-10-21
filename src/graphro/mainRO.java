package graphro;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gdelmondo
 */
public class mainRO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         //graphe G1
         long IStartTime = System.nanoTime();
         Graphe g1r = GrapheListe.deFichier("./data/datafig1r.txt");
         Graphe g1n = GrapheListe.deFichier("./data/datafig1n.txt");
         //Isomorphic iso = new Isomorphism;
         System.out.println("*************************************************************** ");
         System.out.println("Sont les memes G1r & G1r:" + IsomorphismEleve.isIsomorphic(g1r,g1r));
         System.out.println("Sont les memes G1r & G1n :" + IsomorphismEleve.isIsomorphic(g1r,g1n));
         long lEndTime = System.nanoTime();
         long output = lEndTime - IStartTime;
         System.out.println("Elapsed time in milliseconds:" + output/1000000);
         System.out.println("*************************************************************** ");
    }



}
