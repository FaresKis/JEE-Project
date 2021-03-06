/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Entities.Employee;
import Entities.User;
import SessionBeans.EntitiesSB;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/Project"})
public class Project extends HttpServlet {

    @EJB
    EntitiesSB entitiesSB;

    List<Employee> employees;

    HttpSession session;

    boolean selected = true;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String userName = request.getParameter("login");
        String password = request.getParameter("password");
        employees = new ArrayList<>();
        employees = entitiesSB.getAllEmployees();
        session = request.getSession();
        session.setAttribute("listEmp", employees);
        session.setAttribute("errorAdd", false);
        session.setAttribute("emplSelected", true);
        session.setAttribute("emplDeleted", false);

        // Login
        if (userName != null) {
            User loggedUser = entitiesSB.getUser(userName, password);
            if (loggedUser == null){
                String errorConnection = "errorConnection";
                session.setAttribute("errorConnection", errorConnection);
                request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
            } else {
                if (loggedUser.getAdmin()){
                    session.setAttribute("admin", loggedUser);
                    session.setAttribute("errorConnection", "");
                    request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                } else {
                    session.setAttribute("user", loggedUser);
                    session.setAttribute("errorConnection", "");
                    request.getRequestDispatcher("WEB-INF/home.jsp").forward(request, response);
                }
            }
        }
        // Redirection des boutons Ajouter/Modfier/Supprimer
        String action = request.getParameter("action");
        if (action != null && session.getAttribute("admin") != null){
            switch (action) {
                case "Ajouter":
                    session.setAttribute("action", action);
                    request.getRequestDispatcher("WEB-INF/add.jsp").forward(request, response);
                    break;

                case "Modifier":
                    if (request.getParameter("idEmpl") == null){
                        session.setAttribute("emplSelected", false);
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    } else {
                        Integer idEmpl = Integer.parseInt(request.getParameter("idEmpl"));
                        Employee changedEmp = entitiesSB.getSpecificEmployee(idEmpl);
                        session.setAttribute("changedEmp", changedEmp);
                        session.setAttribute("idEmpl", idEmpl);
                        session.setAttribute("action", action);
                        session.setAttribute("emplSelected", true);
                        request.getRequestDispatcher("WEB-INF/modify.jsp").forward(request, response);
                    }
                    break;
                
                case "Supprimer":
                    if (request.getParameter("idEmpl") == null){
                        session.setAttribute("emplSelected", false);
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    } else {
                        Integer idEmpl = Integer.parseInt(request.getParameter("idEmpl"));
                        entitiesSB.deleteEmployee(idEmpl);
                        employees = entitiesSB.getAllEmployees();
                        session.setAttribute("listEmp", employees);
                        session.setAttribute("emplSelected", true);
                        session.setAttribute("emplDeleted", true);
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    }
                    break;
            }
        }

        // Gestion des actions dans les jsp add et modify
        action = (String) session.getAttribute("action");
        if (action != null && session.getAttribute("admin") !=null){
            switch (action) {
                case "Ajouter":
                    if (request.getParameter("retour") != null) 
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    if (request.getParameter("add") != null){
                        String addNom = (String) request.getParameter("addNom");
                        String addPrenom = (String) request.getParameter("addPrenom");
                        String addTeldom = (String) request.getParameter("addTeldom");
                        String addTelport = (String) request.getParameter("addTelport");
                        String addTelpro = (String) request.getParameter("addTelpro");
                        String addAdresse = (String) request.getParameter("addAdresse");
                        String addCodePostal = (String) request.getParameter("addCodePostal");
                        String addVille = (String) request.getParameter("addVille");
                        String addEmail = (String) request.getParameter("addEmail");
                        try {
                            entitiesSB.addNewEmployee(addNom, addPrenom, addTeldom, addTelport, addTelpro, addAdresse, addCodePostal, addVille, addEmail);
                        } catch (Exception e) {
                            session.setAttribute("errorAdd", true);
                            request.getRequestDispatcher("WEB-INF/add.jsp").forward(request, response);
                        }
                        employees = entitiesSB.getAllEmployees();
                        session.setAttribute("listEmp", employees);
                        session.setAttribute("errorAdd", false);
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    }
                
                    break;
                
                case "Modifier":
                    if (request.getParameter("retour") != null) 
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    if (request.getParameter("modify") != null){
                        String modifiedNom = (String) request.getParameter("modifiedNom");
                        String modifiedPrenom = (String) request.getParameter("modifiedPrenom");
                        String modifiedTeldom = (String) request.getParameter("modifiedTeldom");
                        String modifiedTelport = (String) request.getParameter("modifiedTelPort");
                        String modifiedTelpro = (String) request.getParameter("modifiedTelPro");
                        String modifiedAdresse = (String) request.getParameter("modifiedAdresse");
                        String modifedCodePostal = (String) request.getParameter("modifiedCodePostal");
                        String modifiedVille = (String) request.getParameter("modifiedVille");
                        String modifiedEmail = (String) request.getParameter("modifiedEmail");
                    
                        Integer idEmpl = (Integer) session.getAttribute("idEmpl");
                        entitiesSB.updateEmployee(modifiedNom, modifiedPrenom, modifiedTeldom, modifiedTelport, modifiedTelpro, modifiedAdresse, modifedCodePostal, modifiedVille, modifiedEmail, idEmpl);
                        employees = entitiesSB.getAllEmployees();
                        session.setAttribute("listEmp", employees);
                        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
                    }
                    break;
            }
        }

        // Deconnexion
        if (request.getParameter("logout") != null) {
            session.invalidate();
            response.sendRedirect("logout.html");
        }

        if (request.getParameter("restapi") != null) {
            response.sendRedirect(request.getContextPath()+"/webapi");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
