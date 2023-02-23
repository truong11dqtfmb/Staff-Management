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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
class StaffController {
    @Autowired
    private StaffService staffService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger LOGGER = LogManager.getLogger(StaffController.class);

    @GetMapping(value = {"/page/{pageNumber}"})
    String page(Model model, @PathVariable(name = "pageNumber") int currentPage, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir) {
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
    String index(Model model) {
//        model.addAttribute("LIST_STAFF", staffService.findAll());
//        return "staffs";

        LOGGER.info("GET LIST STAFF");

        return page(model, 1, "id", "asc");
    }

    @GetMapping("/create")
    String create(Model model) {
        model.addAttribute("StaffDTO", new StaffDTO());

        LOGGER.info("FORM ADD STAFF");

        return "staff_add";
    }

    @PostMapping("/save")
    String save(@Valid @ModelAttribute("StaffDTO") StaffDTO staffDTO, BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return "staff_add";

        } else {
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

            LOGGER.info("SAVE STAFF");
            return "redirect:/staff";
        }
    }

    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) throws IOException {
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

        LOGGER.info("FORM EDIT STAFF");
        return "staff_edit";
    }

    @PostMapping("/update/{id}")
    String update(@Valid @ModelAttribute("StaffDTO") StaffDTO staffDTO, BindingResult bindingResult, Model model, @PathVariable("id") Long id) throws Exception {
        if (bindingResult.hasErrors()) {
            return "staff_edit";

        } else {

            Optional<Staff> optionalStaff = staffService.findById(id);

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

            LOGGER.info("UPDATE STAFF");
            return "redirect:/staff";
        }
    }

    @GetMapping("/view/{id}")
    String view(Model model, @PathVariable("id") Long id) throws IOException {
        Optional<Staff> staff = staffService.findById(id);

        if (staff.isPresent()) {
            StaffDTO staffDTO = modelMapper.map(staff.get(), StaffDTO.class);

            model.addAttribute("urlImage", staff.get().getPhoto());
            model.addAttribute("StaffDTO", staffDTO);
        } else {
            model.addAttribute("StaffDTO", new StaffDTO());
        }

        LOGGER.info("VIEW DETAIL STAFF");
        return "staff_view";
    }

    @GetMapping("/delete/{id}")
    String delete(@PathVariable("id") Long id) {
        staffService.deleteById(id);

        LOGGER.info("DELETE STAFF");
        return "redirect:/staff";
    }

    @GetMapping("/search")
    String search(Model model, @RequestParam("key_search") Optional<String> key_search) {
        if (key_search.isPresent()) {
            if (!key_search.get().isBlank()) {
                model.addAttribute("LIST_STAFF", staffService.search(key_search.get()));
                return "search";
            }
            model.addAttribute("LIST_STAFF", staffService.findAll());
            return "search";
        }
        model.addAttribute("LIST_STAFF", staffService.findAll());


        LOGGER.info("SEARCH STAFF");
        return "redirect:/staff";

    }

    @GetMapping("/exportToExcel")
    void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=staffs_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Staff> listStaffs = staffService.findAll();

        StaffExportExcel staffExportExcel = new StaffExportExcel(listStaffs);

        staffExportExcel.export(response);

        LOGGER.info("EXPORT TO EXCEL");
    }

    @GetMapping("/exportToCSV")
    void exportToCSV(HttpServletResponse response) throws IOException {
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

        LOGGER.info("EXPORT TO CSV");
    }

    @GetMapping("/exportToPDF")
    void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=staffs_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Staff> listStaffs = staffService.findAll();

        StaffExportPDF staffExportPDF = new StaffExportPDF(listStaffs);

        staffExportPDF.export(response);

        LOGGER.info("EXPORT TO PDF");

    }
}
