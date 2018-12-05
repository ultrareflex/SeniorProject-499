package seniorprojec;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Training {


    ArrayList<Float> Burger = new ArrayList<Float>();
    ArrayList<Float> Soda = new ArrayList<Float>();
    ArrayList<Float> Pizza  = new ArrayList<Float>();
    ArrayList<Float> IceCream  = new ArrayList<Float>();
    Statement stm = null;
    ResultSet rs = null;
    DBconnct connect = null;
    String userID;
    Connection conn = connect.getMysql().getConnection();
    String updateMeditation = "UPDATE eorder SET meditation = ? WHERE id = ?;";
    String updateLeft = "UPDATE eorder SET order1 = ? WHERE id = ?;";
    String updateRight = "UPDATE eorder SET order2 = ? WHERE id = ?;";
    String updateForward = "UPDATE eorder SET order3 = ? WHERE id = ?;";
    String updateBackward = "UPDATE eorder SET order4 = ? WHERE id = ?;";
    PreparedStatement preStm;
    OSCPortIn receiver;
    OSCListener listener;


    public Training(String id) throws SQLException {

        this.userID = id;

    }



	public float trainBurger() {

        messageHandler("/COG/LEFT", Burger);
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {

        }
        receiver.stopListening();
        receiver.close();
        float k = 0;
        for (int i = 0; i<Burger.size(); i++) {

            k += Burger.get(i);

        }

        float average = k/Burger.size();
        //DecimalFormat dc = new DecimalFormat("#.####");
        updateValues(average, updateLeft);

        return average;
    }

    public float trainSoda() {

        messageHandler("/COG/RIGHT", Soda);
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {

        }
        receiver.stopListening();
        receiver.close();
        float k = 0;
        for (int i = 0; i<Soda.size(); i++) {

            k += Soda.get(i);

        }

        float average = k/Soda.size();
        updateValues(average, updateRight);
        return average;
    }

    public float trainPizza() {

        messageHandler("/COG/PUSH", Pizza );
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {

        }
        receiver.stopListening();
        receiver.close();
        float k = 0;
        for (int i = 0; i<Pizza .size(); i++) {

            k += Pizza .get(i);

        }

        float average = k/Pizza .size();
        updateValues(average, updateForward);
        return average;
    }

    public float trainIceCream () {

        messageHandler("/COG/PULL", IceCream );
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end)
        {

        }
        receiver.stopListening();
        receiver.close();
        float k = 0;
        for (int i = 0; i<IceCream.size(); i++) {

            k += IceCream.get(i);

        }

        float average = k/IceCream.size();
        updateValues(average, updateBackward);
        return average;
    }

    private void updateValues(float avg, String query) {

        try {
            preStm = conn.prepareStatement(query);
            preStm.setFloat(1, avg);
            preStm.setString(2, userID);
            preStm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void messageHandler(String address, ArrayList list) {


        try {
            receiver = new OSCPortIn(7400);
            listener = new OSCListener() {
                @Override
                public void acceptMessage(Date date, OSCMessage message) {
                    Object [] args = message.getArguments();
                    String myMessage = args[0].toString();
                    if (message.getAddress().contains(address)) {
                        System.out.println("message received");
                        list.add(Float.parseFloat(myMessage));
                        System.out.println("address is: "+address);
                        System.out.println("Message: "+myMessage);
                        System.out.println("\n");
                    }
                }
            };
            receiver.addListener(address, listener);
            receiver.startListening();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        receiver.stopListening();
        receiver.close();
    }

}