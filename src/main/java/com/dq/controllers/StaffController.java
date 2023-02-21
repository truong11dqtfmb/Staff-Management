package com.dq.controllers;

import com.dq.dto.StaffDTO;
import com.dq.entity.Staff;
import com.dq.export.StaffExportExcel;
import com.dq.export.StaffExportPDF;
import com.dq.services.StaffService;
import com.dq.utils.FileUploadUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(value = {"/page/{pageNumber}"})
    public String page(Model model, @PathVariable(name = "pageNumber") int currentPage, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir){
        Page<Staff> page = staffService.pagefindAll(currentPage, sortField, sortDir);
        long totalItem = page.getTotalElements();
        int totalPages = page.getTotalPages();

        model.addAttribute("totalItem", totalItem);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("LIST_STAFF", page.getContent());

        return "staffs";
    }

    @GetMapping("")
    public String index(Model model) {
//        model.addAttribute("LIST_STAFF", staffService.findAll());
//
//        staffService.findAll().forEach(a -> System.out.println(a.getPhoto()));
//
//        System.out.println("LIST SUCCESS! ");
//        return "staffs";

        return page(model,1,"id","asc");
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("StaffDTO", new StaffDTO());

        System.out.println("CREATE SUCCESS! ");
        return "staff_add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("StaffDTO")  StaffDTO staffDTO, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "staff_add";

        } else {
            System.out.println("StaffDTO: " + staffDTO);

            String image = null;

//        randomNameFileImage
            String randomFileName = String.valueOf(new Date().getTime()) + "_" + staffDTO.getPhoto().getOriginalFilename().toString();

            image = randomFileName;

//       Extention image valid
            if (!(image.contains("png") || image.contains("jpg") || image.contains("jpeg"))) {
                image = "logo.jpg";
            }

//        SaveImage(Util)
            FileUploadUtil.saveFile(randomFileName, staffDTO.getPhoto());

//        Conver DTO -> Entity
            Staff entity = modelMapper.map(staffDTO, Staff.class);

//        Nếu người dùng không chọn image upload thì se mặc định lấy (logo.jpg")
            entity.setPhoto(image);

            staffService.save(entity);
            System.out.println("SAVE SUCCESS! ");
            return "redirect:/staff";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) throws IOException {
        Optional<Staff> staff = staffService.findById(id);


        if (staff.isPresent()) {
            Staff entity = staff.get();
//          Entity -> DTO
            StaffDTO staffDTO = modelMapper.map(staff.get(), StaffDTO.class);

//            Set Photo(Convert String to MultipartFile)
            staffDTO.setPhoto(FileUploadUtil.convertFormStringToMultipartFile(entity.getPhoto()));

            model.addAttribute("urlImage", entity.getPhoto());
            model.addAttribute("StaffDTO", staffDTO);
            model.addAttribute("staffId", entity.getId());
        } else {
            model.addAttribute("StaffDTO", new StaffDTO());
            return "staff_add";

        }

        System.out.println("EDIT SUCCESS! ");
        return "staff_edit";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("StaffDTO") StaffDTO staffDTO,BindingResult bindingResult, Model model, @PathVariable("id") Long id) throws Exception {
        if (bindingResult.hasErrors()) {
            return "staff_edit";

        } else {

            Optional<Staff> optionalStaff = staffService.findById(id);

            System.out.println("StaffDTO: " + staffDTO);

            String image = null;
            if (optionalStaff.isPresent()) {

//            Image null giữ nguyên không save
                if (staffDTO.getPhoto().isEmpty()) {
                    image = optionalStaff.get().getPhoto();

//                Image save mới
                } else {
                    String randomFileName = String.valueOf(new Date().getTime()) + "_" + staffDTO.getPhoto().getOriginalFilename().toString();
                    image = randomFileName;

                    if (!(image.contains("png") || image.contains("jpg") || image.contains("jpeg"))) {
                        image = "logo.jpg";
                    }
                    FileUploadUtil.saveFile(randomFileName, staffDTO.getPhoto());
                }

//            DTO -> Entity
                Staff entity = modelMapper.map(staffDTO, Staff.class);

//            Set Id cũ để không tạo mới
                entity.setId(id);

//        Nếu người dùng không chọn image upload thì se mặc định lấy (logo.jpg")
                entity.setPhoto(image);

                staffService.save(entity);
            }
            System.out.println("UPDATE SUCCESS! ");
            return "redirect:/staff";
        }
    }

    @GetMapping("/view/{id}")
    public String view(Model model, @PathVariable("id") Long id) throws IOException {
        Optional<Staff> staff = staffService.findById(id);

        if (staff.isPresent()) {
            StaffDTO staffDTO = modelMapper.map(staff.get(), StaffDTO.class);

            model.addAttribute("urlImage", staff.get().getPhoto());
            model.addAttribute("StaffDTO", staffDTO);
        } else {
            model.addAttribute("StaffDTO", new StaffDTO());
        }

        System.out.println("VIEW SUCCESS! ");
        return "staff_view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        staffService.deleteById(id);

        System.out.println("DELETE SUCCESS! ");
        return "redirect:/staff";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam("key_search") Optional<String> key_search) {
        if (key_search.isPresent()) {
            if (!key_search.get().isBlank()) {
                model.addAttribute("LIST_STAFF", staffService.search(key_search.get()));
                return "search";
            }
            model.addAttribute("LIST_STAFF", staffService.findAll());
            return "search";
        }
        model.addAttribute("LIST_STAFF", staffService.findAll());
        return "redirect:/staff";

    }

    @GetMapping("/exportToExcel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=staffs_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Staff> listStaffs = staffService.findAll();

        StaffExportExcel staffExportExcel = new StaffExportExcel(listStaffs);

        staffExportExcel.export(response);

        System.out.println("EXPORT TO EXCEL SUCCESS! ");
    }

    @GetMapping("/exportToCSV")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=staffs_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Staff> listStaffs = staffService.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Staff ID", "Full Name", "Email", "Gender", "Phone Number", "Photo", "Salary"};
        String[] nameMapping = {"id", "name", "email", "gender", "phone", "photo", "salary"};


        csvWriter.writeHeader(csvHeader);

        for (Staff staff : listStaffs) {
            csvWriter.write(staff, nameMapping);
        }

        csvWriter.close();
        System.out.println("EXPORT TO CSV SUCCESS! ");
    }

    @GetMapping("/exportToPDF")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=staffs_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Staff> listStaffs = staffService.findAll();

        StaffExportPDF staffExportPDF = new StaffExportPDF(listStaffs);

        staffExportPDF.export(response);
    }
}
