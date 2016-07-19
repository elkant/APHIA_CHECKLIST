/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checklist;

import database.AddTrails;
import database.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Geofrey Nyabuto
 */
public class save_editedSite extends HttpServlet {
HttpSession session;
String hf_id,indicator_id,date,month,year,timestamp,recounted_data,reported_711A,reported_731,reported_DHIS,id;
String found="",entry_type="",nextpage,aphia_staff,moh_staff,marked_found="",userid;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
     session=request.getSession();
     dbConn conn = new dbConn();
     marked_found="";
     if(session.getAttribute("date")!=null){
//       String dates [] = session.getAttribute("date").toString().split("/");
     
       year=session.getAttribute("assessYear").toString();
       month=session.getAttribute("assessMonth").toString();
      
       int assessMonth=Integer.parseInt(month);
       if(assessMonth<10){month="0"+month;}
     date=session.getAttribute("date").toString();
     hf_id=session.getAttribute("hf_id").toString();
     aphia_staff=session.getAttribute("aphia_staff").toString();
     moh_staff=session.getAttribute("moh_staff").toString();
     
//     session.setAttribute("assessYear",year);
//     session.setAttribute("assessMonth",month);
       
     userid=session.getAttribute("userid").toString();
   AddTrails trails = new AddTrails();
   
         System.out.println("hf_ id :  "+hf_id+" year : "+year+" month: "+month);
   
     String check_status="SELECT id FROM marked_checklist WHERE hf_id='"+hf_id+"' && year='"+year+"' && month='"+month+"'";
     conn.rs=conn.st.executeQuery(check_status);
     if(conn.rs.next()==true){
         marked_found=conn.rs.getString(1);
     }
     IdGenerator IDGN = new IdGenerator();
     id=IDGN.current_id();
     timestamp=IDGN.date_key();
     if(marked_found.equals("")){
//         INSERT AS NEW ENTRY..........................
         String insert_mchecklist="INSERT INTO marked_checklist (id,hf_id,aphia_staff,moh_staff,year,month,date,timestamp) VALUES "
                 + " ('"+id+"','"+hf_id+"','"+aphia_staff+"','"+moh_staff+"','"+year+"','"+month+"','"+date+"','"+timestamp+"')";
    conn.st.executeUpdate(insert_mchecklist);
   //    ADD TRAILS TO THE DATABASE
    trails.addTrails(userid, "added site checklist data for health facility: "+hf_id+" data year : "+year+"  and data month : "+month+" :  APHIA Staff : "+aphia_staff+" and moh staff is : "+moh_staff);   
 
     }
         if(!marked_found.equals("")){
//         UPDATE ENTRIES..........................
         String update_mchecklist="UPDATE marked_checklist SET aphia_staff='"+aphia_staff+"',moh_staff='"+moh_staff+"',date='"+date+"',timestamp='"+timestamp+"' WHERE hf_id='"+hf_id+"' && year='"+year+"' && month='"+month+"'";
    conn.st.executeUpdate(update_mchecklist);
  //    ADD TRAILS TO THE DATABASE
    trails.addTrails(userid, "edit site checklist data for health facility: "+hf_id+" data year : "+year+"  and data month : "+month+" : APHIA Staff : "+aphia_staff+" and moh staff is : "+moh_staff);   
 
         }
     
     for(int i=1;i<=21;i++){
         found=indicator_id=recounted_data=reported_711A=reported_731=reported_DHIS="";
             IdGenerator IG = new IdGenerator();
        if(request.getParameter("indicator_id"+i)!=null){ indicator_id = request.getParameter("indicator_id"+i);}
        if(request.getParameter("recounted_data"+i)!=null){ recounted_data = request.getParameter("recounted_data"+i);}
         if(request.getParameter("reported_711A"+i)!=null){ reported_711A = request.getParameter("reported_711A"+i);}
         if(request.getParameter("reported_731"+i)!=null){  reported_731 = request.getParameter("reported_731"+i);}
        if(request.getParameter("reported_DHIS"+i)!=null){  reported_DHIS = request.getParameter("reported_DHIS"+i);}
         timestamp=IG.date_key();
         id=IG.current_id();
        System.out.println(" id  : "+indicator_id+"  rc  :  "+recounted_data+"   711a  :   "+reported_711A+" 731  :  "+reported_731+"   dhis  :  "+reported_DHIS);
//         entry_type=session.getAttribute("entry_type").toString();
//         if(entry_type.equals("both")){nextpage="edit_toolsvalue";}
//          if(entry_type.equals("site")){nextpage="entry_type.jsp";}
         nextpage="site_checklist.jsp";
//       FOR HTC-PITC & VCT  
         if(i>=1 && i<=6){
             System.out.println("htc site                         "+i);
            String htc_checker="SELECT id FROM htc_site WHERE hf_id='"+hf_id+"' && indicator_id='"+indicator_id+"' && year='"+year+"' && month='"+month+"'";
             conn.rs=conn.st.executeQuery(htc_checker);
             if(conn.rs.next()==true){
               found=conn.rs.getString(1);  
             }
           if(!found.equals("")){
//           UPDATE ENTRIES
               String update_htc="UPDATE htc_site SET recounted_data='"+recounted_data+"', reported_711A='"+reported_711A+"', reported_731='"+reported_731+"',reported_DHIS='"+reported_DHIS+"', timestamp='"+timestamp+"' WHERE id='"+found+"'";
               conn.st.executeUpdate(update_htc);
           }
            if(found.equals("")){
//             INSERTED AS A NEW ENTRY   
                String insert_htc="INSERT INTO htc_site (id,hf_id,indicator_id,recounted_data,reported_711A,reported_731,reported_DHIS,year,month,date,timestamp) VALUES"
                        + "('"+id+"','"+hf_id+"','"+indicator_id+"','"+recounted_data+"','"+reported_711A+"','"+reported_731+"','"+reported_DHIS+"','"+year+"','"+month+"','"+date+"','"+timestamp+"')";
         conn.st.executeUpdate(insert_htc);
            } 
             
         }
         
         //       FOR PMTCT-ANC and Labour and Delivery  
         if(i>=7 && i<=15){
            String pmtct_checker="SELECT id FROM pmtct_site WHERE hf_id='"+hf_id+"' && indicator_id='"+indicator_id+"' && year='"+year+"' && month='"+month+"'";
             conn.rs=conn.st.executeQuery(pmtct_checker);
             if(conn.rs.next()==true){
               found=conn.rs.getString(1);  
             }
           if(!found.equals("")){
//           UPDATE ENTRIES
               String update_pmtct="UPDATE pmtct_site SET recounted_data='"+recounted_data+"', reported_711A='"+reported_711A+"', reported_731='"+reported_731+"',reported_DHIS='"+reported_DHIS+"', timestamp='"+timestamp+"' WHERE id='"+found+"'";
               conn.st.executeUpdate(update_pmtct);
           }
            if(found.equals("")){
//             INSERTED AS A NEW ENTRY   
                String insert_pmtct="INSERT INTO pmtct_site (id,hf_id,indicator_id,recounted_data,reported_711A,reported_731,reported_DHIS,year,month,date,timestamp) VALUES"
                        + "('"+id+"','"+hf_id+"','"+indicator_id+"','"+recounted_data+"','"+reported_711A+"','"+reported_731+"','"+reported_DHIS+"','"+year+"','"+month+"','"+date+"','"+timestamp+"')";
         conn.st.executeUpdate(insert_pmtct);
            } 
             
         }
         
         
                  
         //       FOR Care and treatment  
         if(i>=16 && i<=19){
            String caretreatment_checker="SELECT id FROM caretreatment_site WHERE hf_id='"+hf_id+"' && indicator_id='"+indicator_id+"' && year='"+year+"' && month='"+month+"'";
             conn.rs=conn.st.executeQuery(caretreatment_checker);
             if(conn.rs.next()==true){
               found=conn.rs.getString(1);  
             }
           if(!found.equals("")){
//           UPDATE ENTRIES
               String update_caretreatment="UPDATE caretreatment_site SET recounted_data='"+recounted_data+"', reported_711A='"+reported_711A+"', reported_731='"+reported_731+"',reported_DHIS='"+reported_DHIS+"', timestamp='"+timestamp+"' WHERE id='"+found+"'";
               conn.st.executeUpdate(update_caretreatment);
           }
            if(found.equals("")){
//             INSERTED AS A NEW ENTRY   
                String insert_caretreatment="INSERT INTO caretreatment_site (id,hf_id,indicator_id,recounted_data,reported_711A,reported_731,reported_DHIS,year,month,date,timestamp) VALUES"
                        + "('"+id+"','"+hf_id+"','"+indicator_id+"','"+recounted_data+"','"+reported_711A+"','"+reported_731+"','"+reported_DHIS+"','"+year+"','"+month+"','"+date+"','"+timestamp+"')";
         conn.st.executeUpdate(insert_caretreatment);
            } 
             
         }
         
                       
         //       FOR TB / HIV 
         if(i>=20 && i<=21){
            String tb_checker="SELECT id FROM tb_site WHERE hf_id='"+hf_id+"' && indicator_id='"+indicator_id+"' && year='"+year+"' && month='"+month+"'";
             conn.rs=conn.st.executeQuery(tb_checker);
             if(conn.rs.next()==true){
               found=conn.rs.getString(1);  
             }
           if(!found.equals("")){
//           UPDATE ENTRIES
               String update_tb="UPDATE tb_site SET recounted_data='"+recounted_data+"', reported_711A='"+reported_711A+"', reported_731='"+reported_731+"',reported_DHIS='"+reported_DHIS+"', timestamp='"+timestamp+"' WHERE id='"+found+"'";
               conn.st.executeUpdate(update_tb);
           }
            if(found.equals("")){
//             INSERTED AS A NEW ENTRY   
                String insert_tb="INSERT INTO tb_site(id,hf_id,indicator_id,recounted_data,reported_711A,reported_731,reported_DHIS,year,month,date,timestamp) VALUES"
                        + "('"+id+"','"+hf_id+"','"+indicator_id+"','"+recounted_data+"','"+reported_711A+"','"+reported_731+"','"+reported_DHIS+"','"+year+"','"+month+"','"+date+"','"+timestamp+"')";
         conn.st.executeUpdate(insert_tb);
            } 
             
         }   
     }
    }
   else{
       session.setAttribute("checklist_added", "NO SESSION");
   }
    System.out.println("saved edited and redirected to  :  "+nextpage);
      session.setAttribute("saved_success", "<font color=\"blue\">Site Checklist Details Edited Successfully.</font>");
  session.setAttribute("savedChecklist", "savedChecklist");
      response.sendRedirect(nextpage); 
     
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(save_editedSite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(save_editedSite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
