package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeveloperController {

private Map<Integer, Developer> developers;
private Taxable developerTax;

    @PostConstruct
    public void init() {
        this.developers = new HashMap<>();
    }
    @Autowired
    public DeveloperController(Taxable developerTax) {
        this.developerTax= developerTax;

    }

    @GetMapping("/developers")
    public List<Developer> getDeveloperList(){
        return developers.values().stream().toList();
    }
    @GetMapping("/developers/{id}")
    public Developer returnDeveloperById(@PathVariable int id){
        if(!developers.containsKey(id)){
            System.out.println("id bulunmamakta !!!");
        }
        return developers.get(id);
    }
    @PostMapping("/developers")
    public void returnPost(@RequestParam int id, @RequestParam String name, @RequestParam double salary, @RequestParam Experience experience){
        Developer developer =null;


        switch (experience){
            case JUNIOR:
                developer= new JuniorDeveloper(id,name,salary - calculateTax(salary, developerTax.getSimpleTaxRate()));

          break;
            case MID:
                developer= new MidDeveloper(id,name,salary - calculateTax(salary, developerTax.getMiddleTaxRate()));
            break;
            case SENIOR:
                developer= new SeniorDeveloper(id,name,salary - calculateTax(salary, developerTax.getUpperTaxRate()));
            break;
            default:
                System.out.println("yanlış experience tipi");
        }

        developers.put(id,developer);

    }
    @PutMapping("/developers/{id}")
    public void returnMapById(@PathVariable int id,@RequestBody Developer developer){
        if(developers.containsKey(id)){
            developers.put(id,developer);
        }

    }
    @DeleteMapping("/developers/{id}")
    public Developer deleteMap(@PathVariable int id){
       return developers.remove(id);
    }
    private double calculateTax(double salary, double taxRate) {
        return salary * (taxRate / 100);
    }

    public Map<Integer, Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Map<Integer, Developer> developers) {
        this.developers = developers;
    }
}
