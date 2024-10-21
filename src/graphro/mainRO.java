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

        List<Sommet> vertices = Arrays.asList(
                new Sommet("8_rue_mauve", 0),
                new Sommet("22_rue_verte", 0),
                new Sommet("3_rue_marron", 0),
                new Sommet("10_rue_rouge", 0),
                new Sommet("depot", 0)
        );

        Graphe weightedGraph = createWeightedGraph(grapheInitial, vertices);

        Graphe grapheTranforme = GrapheListe.deFichier("./data/graphe.txt");

        System.out.println(IsomorphismEleve.isIsomorphic(weightedGraph, grapheTranforme));

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

    public static Graphe createWeightedGraph(Graphe originalGraph, List<Sommet> vertices) {
        Graphe newGraph = new GrapheListe(vertices.size());

        // Add vertices to the new graph
        for (Sommet vertex : vertices) {
            newGraph.ajouterSommet(vertex);
        }

        // Add edges with distances to the new graph
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Sommet s1 = vertices.get(i);
                Sommet s2 = vertices.get(j);
                int distance = dijkstra(originalGraph, s1).get(s2);
                newGraph.ajouterArc(s1, s2, distance);
                //newGraph.ajouterArc(s2, s1, distance); // For symmetrical edges
                //TODO: Je sais pas si c'est nécessaire
            }
        }

        return newGraph;
    }


}
