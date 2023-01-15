package com.example.GeneratePdfHtml.Controllerpage;

import com.example.GeneratePdfHtml.Model.Student;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Properties;

@Controller
public class pageController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/indexPage")
    public String indexPage(){
        return "Tuition_certificate.html";
    }


    @PostMapping("/saveStudent")
    public Student addStudent(@RequestBody Student student ){
        return  mongoTemplate.save(student);
    }


    @PostMapping("/CinStudent")
    public HttpEntity<byte[]> getStudentByCin(@RequestParam(name = "mc", defaultValue = "") String motCle){

        try {
            Query query= new Query();
            query.addCriteria(Criteria.where("cin").is(motCle));
            Student student =mongoTemplate.findOne(query, Student.class);
            return  pdf("pdtHtml.pdf",student);
            /* return ResponseEntity.ok(student);*/
        }catch (Exception e)
        {
            System.out.print("Error :"+e);
            return null;
        }

    }


    public HttpEntity<byte[]> pdf(  String File, Student student ) throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init( p );
        Template template = Velocity.getTemplate("templates/index.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", student.getFirstname());
        context.put("lastname",student.getLastname());
        context.put("Cin",student.getCity());
        context.put("Birthday",student.getBirthday());
        context.put("genDateTime", LocalDateTime.now().toString());
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        System.out.println(writer.toString());
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        b = generateHtmltoPdf(writer.toString());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + File.replace(" ", "_"));
        header.setContentLength(b.toByteArray().length);
        return new HttpEntity<byte[]>(b.toByteArray(), header);
    }
    public ByteArrayOutputStream generateHtmltoPdf (String html){
        String pdfPath="";
        PdfWriter pdfWriter= null;

        Document document = new Document();
        try {
            document =new Document();
            document.addAuthor("otmane");
            document.addAuthor("otmane");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("otmanetahri.ga");
            document.addTitle("HTML to PDF using itext");
            document.setPageSize(PageSize.LETTER);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, b);
            document.open();
            XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
            xmlWorkerHelper.getDefaultCssResolver(true);
            xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(
                    html));
            document.close();
            System.out.println("HTML Generate to PDF successfully");
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }




    }
}
