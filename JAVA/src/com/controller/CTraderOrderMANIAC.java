/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import TestModules.JTableDataPopulation.JsonParsing;
import UserObjects.SingleOrder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author csavas
 */
public class CTraderOrderMANIAC {

   static ArrayList<SingleOrder> pendings = new ArrayList<>();
    static ArrayList<SingleOrder> executed = new ArrayList<>();
            
    static public void setPendings(ArrayList<SingleOrder> pendings) {
        CTraderOrderMANIAC.pendings = pendings;
    }

    static public void setExecuted(ArrayList<SingleOrder> executed) {
        CTraderOrderMANIAC.executed = executed;
    }

    public static ArrayList<SingleOrder> getPendings() {
        return pendings;
    }

    public static ArrayList<SingleOrder> getExecuted() {
        return executed;
    }
    
    public static TableModel getPRTableModel() {
        if(pendings.isEmpty()){
            return new DefaultTableModel();
        } else { 
            ArrayList<SingleOrder> objList = (ArrayList) pendings;
            return new TraderPendingRequestsTableModel(objList);
        }       
    }
    
    public static TableModel getOHTableModel() {
        if(executed.isEmpty()){
            return new DefaultTableModel();
        } else { 
            ArrayList<SingleOrder> objList = (ArrayList) executed;
            return new PMOrderHistoryTableModel(objList);
        }

    }
    
    public static TableModel getTableModel() {
        ArrayList objList = (ArrayList) updateOrders();
        return new PMPendingRequestTableModel(objList);
    }

    public static List<SingleOrder> updateOrders() {
        String currUsername = CMAIN.reportUser().getUsername();
        HttpResponse<JsonNode> resp;
        try {
            resp = Unirest.get("http://139.59.17.119:8080/api/trader/orders/" + currUsername)
                    .header("content-type", "application/json")
                    .asJson();

            //THIS IS THE JSONRESPONSE TURNED INTO JSONOBJECT  
            JSONObject myRespO = new JSONObject(resp.getBody());
            JSONArray arrJson = myRespO.getJSONArray("array");
            //GET ORDERS FROM ARRAY
            List<SingleOrder> arrayOrders = new ArrayList<>();

            for (int i = 0; i < arrJson.length(); i++) {
                JSONObject currentOrder = arrJson.getJSONObject(i);
                SingleOrder currentSingleOrder = JsonParsing.parseJsonToSingleOrderObject(currentOrder.toString());
                arrayOrders.add(currentSingleOrder);
            }
            System.out.println("Inside Controller : \n"+arrayOrders);
            return arrayOrders;
        } catch (UnirestException | JSONException ex) {
            return null;
        }
    }

}
