package org.usfirst.frc.team2771.libs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
class PIDNetworkTuner implements Runnable {

    private HashMap<String, PIDData> controllerData;
    private ServerSocket server;
    private Socket connection;
    private InputStream input;
    private OutputStream output;

    private static PIDNetworkTuner instance;

    private PIDNetworkTuner() {
        controllerData = new HashMap<>();
        try {
            server = new ServerSocket(5800);
        } catch (IOException ex) {
            Logger.getLogger(PIDNetworkTuner.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.setName("PID Network Controller Thread");
        t.start();
    }

    static PIDNetworkTuner getInstance() {
        if (instance == null) {
            instance = new PIDNetworkTuner();
        }
        return instance;
    }

    void addPIDController(String name) {
        controllerData.put(name, new PIDData());
//        System.out.println(controllerData.size());
        System.out.println(name);
    }
    
    boolean networkHasP(String name) {
//        System.out.println(name);
//        System.out.println(controllerData.get(name));
        return controllerData.get(name).pc;
    }
    
    boolean networkHasI(String name) {
        return controllerData.get(name).ic;
    }
    
    boolean networkHasD(String name) {
        return controllerData.get(name).dc;
    }
    
    boolean networkHasSetpoint(String name) {
        return controllerData.get(name).sc;
    }

    double getP(String name) {
        return controllerData.get(name).p;
    }

    double getI(String name) {
        return controllerData.get(name).i;
    }

    double getD(String name) {
        return controllerData.get(name).d;
    }

    double getSetpoint(String name) {
        return controllerData.get(name).setpoint;
    }

    synchronized void update(String name, double result, double error, double robot_P,
            double robot_I, double robot_D, double robot_Setpoint) {
        String data = name + ":" + result + ":" + error + ":" + robot_P + ":"
                + robot_I + ":" + robot_D + ":" + robot_Setpoint;
        try {
            byte[] dataBytes = PrimitiveSerializer.toByteArray(data);
            short length = (short) dataBytes.length;
            byte[] lengthBytes = PrimitiveSerializer.toByteArray(length);
            output.write(lengthBytes);
            output.write(dataBytes);
//            System.out.println("Done writing update: " + name);
        } catch (IOException ex) {
            Logger.getLogger(PIDNetworkTuner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            // ignore
        }
    }

    private synchronized void reconnect() {
        boolean retry = true;
        while (retry) {
            try {
                if (input != null) {//close if open
                    input.close();
                }
                if (output != null) {//close if open
                    output.close();
                }
                if (connection != null) {//close if open
                    connection.close();
                }
            } catch (IOException ex) {
            }

            try {
                connection = server.accept();
                output = connection.getOutputStream();
                input = connection.getInputStream();
            } catch (IOException ex) {
                Logger.getLogger(PIDNetworkTuner.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                retry = false;
            }
        }
    }

    void flush() {
        if (connection == null) {
            reconnect();
        }
        try {
            output.flush();
        } catch (IOException ex) {
            reconnect();
        }
    }

    @Override
    public void run() {
        reconnect();
        while (true) {
            try {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }

                flush();

                while (input.available() > 0 || input.available() > 0) {
                    System.out.println("DATA!!!");
                    byte one = (byte) input.read();
                    byte two = (byte) input.read();

                    short length = PrimitiveSerializer.bytesToShort(new byte[]{one, two});
                    System.out.println(length);
                    
                    byte[] data = new byte[length];
                    for (int i = 0; i < length; i++) {
                        data[i] = (byte) input.read();
                    }

                    String updateData = PrimitiveSerializer.bytesToString(data);
                    String[] dataString = updateData.split(":");
                    System.out.println(updateData);
                    String name = dataString[0];
                    PIDData pidData = controllerData.get(name);
                    if (dataString[1].equals("-")) {
                        pidData.pc = false;
                    } else {
                        pidData.pc = true;
                        pidData.p = Double.parseDouble(dataString[1]);
                    }
                    if (dataString[2].equals("-")) {
                        pidData.ic = false;
                    } else {
                        pidData.ic = true;
                        pidData.i = Double.parseDouble(dataString[2]);
                    }
                    if (dataString[3].equals("-")) {
                        pidData.dc = false;
                    } else {
                        pidData.dc = true;
                        pidData.d = Double.parseDouble(dataString[3]);
                    }
                    if (dataString[4].equals("-")) {
                        pidData.sc = false;
                    } else {
                        pidData.sc = true;
                        pidData.setpoint = Double.parseDouble(dataString[4]);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(PIDNetworkTuner.class.getName()).log(Level.SEVERE, null, ex);
                reconnect();
            }
        }
    }

    private static class PIDData {
        boolean pc, ic, dc, sc;
        double p, i, d, setpoint;
    }
}
