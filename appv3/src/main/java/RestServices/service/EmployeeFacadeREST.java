/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices.service;

import Entities.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author fareskissoum
 */
@Stateless
@Path("/employee")
public class EmployeeFacadeREST extends AbstractFacade<Employee> {

    @PersistenceContext(unitName = "m1se_appv2_war_1.0PU")
    private EntityManager em;

    public EmployeeFacadeREST() {
        super(Employee.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Employee emp) {
        super.create(emp);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Employee emp) {
        Employee modifiedEmployee = em.find(Employee.class,id);
        modifiedEmployee.setNom(emp.getNom());
        modifiedEmployee.setPrenom(emp.getPrenom());
        modifiedEmployee.setTelDomicile(emp.getTelDomicile());
        modifiedEmployee.setTelPortable(emp.getTelPortable());
        modifiedEmployee.setTelPro(emp.getTelPro());
        modifiedEmployee.setAdresse(emp.getAdresse());
        modifiedEmployee.setCodePostal(emp.getCodePostal());
        modifiedEmployee.setVille(emp.getVille());
        modifiedEmployee.setEmail(emp.getEmail());
        em.merge(modifiedEmployee);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Employee find(@PathParam("id") Integer id) {
        return em.find(Employee.class,id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Employee> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Employee> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
