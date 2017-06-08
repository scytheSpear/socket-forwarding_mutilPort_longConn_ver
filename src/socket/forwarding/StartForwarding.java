/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.forwarding;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author user
 */
public class StartForwarding {

    public static void main(String[] args) {
        String destinationHost;
        String portGroup;
        String[] portGroupArray;

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("socket-forwarding-conf.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read properties file");
            System.exit(-1);
        }

        destinationHost = properties.getProperty("DESTINATION_HOST");
        System.out.println("read host value " + destinationHost + " from properties file");
        portGroup = properties.getProperty("PORT_GROUP");
        System.out.println("read port value " + portGroup + " from properties file");

        if (destinationHost != null && portGroup != null) {
            if (portGroup.contains("-")) {
                portGroupArray = portGroup.trim().split(";");

                for (String group : portGroupArray) {
                    int sourcePort = Integer.valueOf(group.trim().split("-")[0]);
                    int destinationPort = Integer.valueOf(group.trim().split("-")[1]);

                    SocketForwarding sForwarding = new SocketForwarding(sourcePort, destinationHost, destinationPort);
                    sForwarding.start();

                }

            } else {
                System.err.println("port group property has wrong format, \" xxxxx-xxxxx;xxxxx-xxxxx \" is the correct format");
            }
        } else {
            System.err.println("missing properties or properties have null value in file");
        }

    }
}
