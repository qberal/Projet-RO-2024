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

        List<Sommet> vertices = Arrays.asList(
                new Sommet("8_rue_mauve", 0),
                new Sommet("22_rue_verte", 0),
                new Sommet("3_rue_marron", 0),
                new Sommet("10_rue_rouge", 0),
                new Sommet("depot", 0)
        );

        Graphe weightedGraph = createWeightedGraph(grapheInitial, vertices);

        System.out.println(weightedGraph);

        Graphe grapheTranforme = GrapheListe.deFichier("./data/graphe.txt");
        
        System.out.println(IsomorphismEleve.isIsomorphic(weightedGraph, grapheTranforme));

        // For some reason there are negative values in the graph.
        // Take care of those mfs.
        for (Sommet s1 : weightedGraph.sommets()) {
            for (Sommet s2 : weightedGraph.sommets()) {
                int value = weightedGraph.valeurArc(s1, s2);
                if (value < 0) {
                    weightedGraph.enleverArc(s1, s2);
                    weightedGraph.ajouterArc(s1, s2, -value);
                }
            }
        }

        ArrayList<Sommet> bestPath = findBestPath(weightedGraph);
        int bestCost = calculateTourCost(bestPath, weightedGraph);
        System.out.println("Best path: " + bestPath + " with cost " + bestCost);
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
            }
        }

        return newGraph;
    }

    /// Fonction utilitaire pour calculer le coût du tour actuel
    public static int calculateTourCost(ArrayList<Sommet> tour, Graphe g) {
        int cost = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            System.out.println(tour.get(i) + " -> " + tour.get(i + 1) + " : " + g.valeurArc(tour.get(i), tour.get(i + 1)));
            // abs(g.valeurArc(tour.get(i), tour.get(i + 1)))
            cost += g.valeurArc(tour.get(i), tour.get(i + 1));
        }
        cost += g.valeurArc(tour.get(tour.size() - 1), tour.get(0));
        return cost;
    }

    /// Fonction pour trouver une borne inférieure en utilisant une approximation, ici un simple MST
    // public static int minimumRemainingCost(ArrayList<Sommet> tour, Graphe g) {
    //     int minimumRemainingCost = calculateTourCost(tour, g);

    //     for (Sommet s : g.sommets()) {
    //         if (tour.contains(s)) {
    //             continue;
    //         }
    //         int min = Integer.MAX_VALUE;
    //         for (Sommet s2 : g.sommets()) {
    //             if (tour.contains(s2) || s == s2) {
    //                 continue;
    //             }
    //             min = Math.min(min, g.valeurArc(s, s2));
    //         }
    //         minimumRemainingCost += min;
    //     }

    //     return minimumRemainingCost;
    // }

    /// Algorithme de Branch-and-Bound
    public static ArrayList<Sommet> findBestPath(ArrayList<Sommet> start, int bestCost, Graphe g) {
        if (start.size() == g.sommets().size()) {
            return start;
        }

        ArrayList<Sommet> bestTour = null;

        for (Sommet s : g.sommets()) {
            if (start.contains(s)) {
                continue;
            }

            ArrayList<Sommet> newStart = new ArrayList<>(start);
            newStart.add(s);

            // int minimumRemainingCost = minimumRemainingCost(newStart, g);
            // if (minimumRemainingCost >= bestCost) {
            //     continue;
            // }
            
            ArrayList<Sommet> newTour = findBestPath(newStart, bestCost, g);
            if (newTour == null) {
                continue;
            }

            int newCost = calculateTourCost(newTour, g);
            if (newCost < bestCost) {
                bestCost = newCost;
                bestTour = newTour;
            }
        }

        return bestTour;
    }

    public static ArrayList<Sommet> findBestPath(Graphe g) {
        ArrayList<Sommet> start = new ArrayList<>();
        start.add(new Sommet("depot", 0));

        ArrayList<Sommet> bestTour = findBestPath(start, Integer.MAX_VALUE, g);
        bestTour.add(new Sommet("depot", 0));

        return bestTour;
    }
}
