package server;

import rmi.DetRMI;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
/**
 * Implements DetRMI, realizing array operations.
 * @author tomek
 */
public class Server implements DetRMI {
    
    private GUI gui;
    /**
     * Creates new GUI.
     * @param g 
     */
    public Server(GUI g) {
        gui = g;
    }
    /**
     * Realizing connection operations.
     */
    public void bindStub() {
        try {
            //creates registry
            LocateRegistry.createRegistry(1099);
            DetRMI stub = (DetRMI) UnicastRemoteObject.exportObject(this, 1099);
            Registry reg = LocateRegistry.getRegistry();
            //DetRMI is the name of interface from RMI project
            reg.bind("DetRMI", stub);
            gui.getTextArea().setText(">>>>> Serwer gotowy do pracy.");
        } catch(Exception e) {
            gui.getTextArea().setText(gui.getTextArea().getText() + "\n>>>>> Wystąpił błąd: " + e.toString());
        }
    }
    /**
     * Realizing function det(table).
     * Writing log.
     * @param m
     * @param table
     * @return return of function det(table).
     * @throws RemoteException 
     */
    @Override
    public double determinant(double [][] table) throws RemoteException{
        
        int size = table.length;
        gui.getTextArea().setText(gui.getTextArea().getText() + 
                "\n Zdalne obliczanie wyznacznika na mecierzy \n tablica["+size+"]["+size+"]");
        
        return det(table,size);
                
    }
    /**
     * Calculates determinant of matrix, using La Place's algorithm.
     * @param table
     * @param size
     * @return determinant
     * @throws RemoteException 
     */
    public double det(double [][] table, int size) throws RemoteException {
                          
	double buffer[][];
        double result = 0;
	if (size == 1) {
            return table[0][0];
	} 
        else {
            buffer = new double[size - 1][size - 1];
            for (int i = 0; i < size; i++){
		for (int j = 0; j < size - 1; j++){
                    for (int k = 0; k < size - 1; k++){
			if (k<i){
                            buffer[j][k] = table[j + 1][k];
			}
			else{
                            buffer[j][k] = table[j + 1][k+1];
			}							
                    }
		}
                if (i % 2 == 0){
                    result += table[0][i] * det(buffer, size - 1);
		} 
		else{
                    result -= table[0][i] * det(buffer, size - 1);
		}
            }
            return result;
	}
    }	    
}