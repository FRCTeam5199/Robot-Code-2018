/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author watom
 */
public class Listener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 1180;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] buf = new byte[10000];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            buf = new byte[10000];
            packet = new DatagramPacket(buf,buf.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }

            byte[] data = packet.getData();

            //int length = ByteUtils.toInt(ByteUtils.portionOf(data, 0, 4));

            String recieve = new String(data,0, packet.getLength());
            System.out.print(recieve);
           // System.out.println(recieve.length());
        }
    }

}
