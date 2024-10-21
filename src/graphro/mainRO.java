package graphro;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

/**
 * @author gdelmondo
 */
public class mainRO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Graphe grapheInitial = GrapheListe.deFichier("./data/grapheInitial.txt");
        System.out.println(grapheInitial);

        Sommet startNode = grapheInitial.sommets().iterator().next();

        Map<Sommet, Integer> distances = dijkstra(grapheInitial, startNode);

        for (Map.Entry<Sommet, Integer> entry : distances.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

    }

    public static Map<Sommet, Integer> dijkstra(Graphe g, Sommet startNode) {
        Map<Sommet, Integer> distances = new HashMap<>();
        PriorityQueue<Sommet> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<Sommet> visited = new HashSet<>();

        // Initialize distances
        for (Sommet s : g.sommets()) {
            distances.put(s, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Sommet current = pq.poll();
            if (!visited.add(current)) {
                continue;
            }

            for (Sommet neighbor : g.sommets()) {
                if (g.existeArc(current, neighbor)) {
                    int newDist = distances.get(current) + g.valeurArc(current, neighbor);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        pq.add(neighbor);
                    }
                }
                // Check the reverse direction for undirected graph
                if (g.existeArc(neighbor, current)) {
                    int newDist = distances.get(current) + g.valeurArc(neighbor, current);
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        pq.add(neighbor);
                    }
                }
            }
        }

        return distances;
    }

}
