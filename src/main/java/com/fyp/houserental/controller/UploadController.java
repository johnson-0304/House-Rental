package com.fyp.houserental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.houserental.domain.House;
import com.fyp.houserental.domain.ResultInfo;
import com.fyp.houserental.domain.User;
import com.fyp.houserental.service.HouseService;
import com.fyp.houserental.service.OrderService;
import com.fyp.houserental.util.CommonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

@Controller
public class UploadController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtils commonUtils;


    @RequestMapping("/uploadServlet")
    public void uploadServlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> map = request.getParameterMap();
        House house = new House();
        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user != null) {
            try {
                BeanUtils.populate(house, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //<editor-fold desc="Agreement-Content">
            //agreemnet
            String agreement = "THIS TENANCY AGREEMENT is made this\n" +
                    "\n" +
                    "BETWEEN\n" +
                    "\n" +
                    "The parties whose names and descriptions are stated as House Owner of the Schedule annexed hereto which expression shall where the context so admits include in the case of body corporate its successor-in-title and permitted assigns of the one part;\n" +
                    "\n" +
                    "AND\n" +
                    "\n" +
                    "The parties whose names and descriptions are stated as Tenant of the Schedule annexed hereto which expression shall where the context so admits include in the case of body corporate its successor-in-title and permitted assigns of the other part.\n" +
                    "\n" +
                    "\n" +
                    "WHEREAS:-\n" +
                    "\n" +
                    "\n" +
                    "(A)\tThe House Owner is the registered proprietor of all that parcel of land described in Section 3 of the Schedule annexed hereto (hereinafter referred to as “the Property”).\n" +
                    "\n" +
                    "(B)\tThe House Owner is desirous of letting and the Tenant is desirous of taking a tenancy of the premises described in Section 4 of the Schedule annexed hereto (hereinafter referred to as “the Demised Premises”).\n" +
                    "\n" +
                    "\n" +
                    "WHEREFORE IT IS MUTUALLY AGREED as follows:-\n" +
                    "\n" +
                    "\n" +
                    "1.\tTERM AND COMMENCEMENT DATE\n" +
                    "\n" +
                    "1.1\tThe House Owner shall let and the Tenant shall take a tenancy (hereinafter referred to as “this Tenancy”) of the Demised Premises for a period stated in Section 5(i) of the Schedule annexed hereto (hereinafter referred to as “the Term”), and commencing on the date stated in Section 6(i) of the Schedule annexed hereto (hereinafter referred to as “the Commencement Date”) and expiring on the date stated in Section 6(ii) of the Schedule annexed hereto (hereinafter referred to as \"the Expiry Date\").\n" +
                    "\n" +
                    "\n" +
                    "2.\tRENTAL\n" +
                    "\n" +
                    "2.1\tThe rental for the Demised Premises shall be as stated in Section 7 of the Schedule annexed hereto (hereinafter referred to as “the Rental”) and shall be payable in the manner described in Section 8 of the Schedule annexed hereto for the purpose described in Section 10 of the Schedule annexed hereto (hereinafter referred to as “the Purpose”).\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "3.\tSECURITY DEPOSIT\n" +
                    "\n" +
                    "3.1\tThe Tenant hereby agrees to pay the House Owner on the execution of this Tenancy Agreement the sum stated in Section 9(i) of the Schedule annexed hereto by way of a deposit as security for the due observance and performance by the Tenant of its covenants herein contained (hereinafter referred to as “the Security Deposit”) wherein the Security Deposit shall be subject to the provisions of this Tenancy and in the absence of any breach by the Tenant of the terms and conditions herein contained be returned to the Tenant free of interest within one (1) month from the termination of this Tenancy PROVIDED ALWAYS that the Security Deposit shall not be treated as towards payment of rent.\n" +
                    "\n" +
                    "3.2\tThe Tenant hereby agrees to pay the House Owner on the execution of this Tenancy Agreement the sum stated in Section 9(ii) of the Schedule annexed hereto by way of a deposit for payment of electricity and water bills, costs of repairs and other sums payable by the Tenant under this Tenancy (if any) (hereinafter referred to as \"the Utility Deposit\") and which shall be refunded to the Tenant free of interest within one (1) month from the termination of this Tenancy hereby created.\n" +
                    "\n" +
                    "3.3\tThe Tenant shall not utilise any of the Security Deposit and/or Utility Deposit to set off or pay for any outstanding rent or sums or moneys due and payable under this Tenancy.\n" +
                    "\n" +
                    "3.4\tIn the event that any of the parties hereto shall desire to terminate this Tenancy before the expiration of the Term hereby granted, the party intending to terminate this Tenancy shall give to the other party three (3) months’ notice in writing of its/their intention so to do or pay three (3) months’ rental in lieu of such notice and at the expiry of which notice or upon payment of such sum in lieu of notice, whichever the case may be, this Tenancy Agreement shall be deemed terminated.\n" +
                    "\n" +
                    "\n" +
                    "4.\tTENANT’S COVENANT\n" +
                    "\n" +
                    "4.1\tThe Tenant hereby covenants with the House Owner as follows:-\n" +
                    "\n" +
                    "(a)\tTo pay the Rental on the day and in the manner provided herein.\n" +
                    "\n" +
                    "(b)\tTo punctually pay for all charges for the water, electricity, telephone and other utilities (if any) used and consumed in the Demised Premises.\n" +
                    "\n" +
                    "(c)\tNot to use the Demised Premises for any illegal or immoral purposes.\n" +
                    "\n" +
                    "(d)\tNot to bring or store or permit or suffer to be brought or stored on the Demised Premises arms ammunition or unlawful goods gunpowder saltpetre or any combustible substance or any goods which are of a noxious or dangerous or hazardous nature.\n" +
                    "\n" +
                    "(e)\tTo keep and maintain the exterior and interior of the Demised Premises including the toilet facilities, flooring and interior plaster or other surfacing material or rendering on walls and ceilings and the House Owner’s fixtures and fittings therein including all doors, windows, electrical installations, lightings, power sockets, locks, shutters, water taps, toilets, drainage, etc. in good, clean, tenantable, substantial and proper repair and condition and the Tenant shall be responsible for repairs and/or replacements thereof and deliver up the same to the House Owner at the expiration or sooner determination of this Tenancy in like condition, fair wear and tear excepted.\n" +
                    "\n" +
                    "\n" +
                    "(f)\tNot to assign or underlet or sublet or part with possession or the occupation or the use of the Demised Premises or any part thereof without the prior consent of the House Owner.\n" +
                    "\n" +
                    "(g)\tTo repair and make good or pay the House Owner for the cost of repairing or making good such damage caused by acts/omission of vandalism or negligence and/or willful neglect of the Tenant.\n" +
                    "\n" +
                    "(h)\tTo permit the House Owner and/or the House Owner’s agents surveyors and/or workmen to enter upon the Demised Premises at all reasonable times for the purpose of viewing the condition thereof or for doing such reasonable work as may be thought fit.\n" +
                    "\n" +
                    "(i)\tTo observe and comply with all notices served in respect of the Demised Premises and rules and regulations of the local authorities.\n" +
                    "\n" +
                    "(j)\tNot to make any renovations or alterations to the Demised Premises without the prior written consent from the House Owner and, where approval of any relevant authorities is required, without the prior written consent of the relevant authorities first had been obtained.\n" +
                    "\n" +
                    "(k)\tBefore the expiry of this Tenancy to give the House Owner three (3) months’ notice in writing in advance as to whether or not the Tenant is desirous of entering into a new tenancy with the House Owner.\n" +
                    "\n" +
                    "(l)\tAt the expiration or sooner determination of the Term to peacefully and quietly deliver up the Demised Premises to the House Owner in the original condition as the same were delivered to the Tenant as at the date of this Tenancy, at the Tenant’s own cost, if any, to be incurred, fair wear and tear excepted.\n" +
                    "\n" +
                    "(m)\tNot to do or permit or suffer anything to be done in or upon the Demised Premises or any part thereof which may be or become a nuisance or annoyance to or in any way interfere with the quiet enjoyment of the occupants of the neighboring premises.\n" +
                    "\n" +
                    "(n)\tTo permit the House Owner to transfer, sell or dispose of the Demised Premises at any time provided that the House Owner shall notify the new purchaser or transferee of this Tenancy and procure the new purchaser or transferee to continue to allow the Tenant to exercise its rights herein.\n" +
                    "\n" +
                    "(o)\tNot to obstruct or litter or in any way make untidy the Demised Premises or the corridors, passages, staircases, entrances, or the areas designated for parking of the Demised Premises or the Property.\n" +
                    "\n" +
                    "(p)\tNot to use the Demised Premises or any part thereof for carrying any other business or purpose or activity so as to cause unreasonable accumulation of dirt, rubbish or debris of any sort in or outside the Demised Premises or which causes an undesirable amount of noise or which in the opinion of the House Owner is undesirable or unsuitable for the other tenants or occupants of the Property, adjacent shophouses or neighboring premises.\n" +
                    "\n" +
                    "\n" +
                    "(q)\tTo adopt every reasonable and foreseeable precaution necessary to prevent fire and explosion and not to do or permit or suffer to be done anything whereby any policy of insurance of the Demised Premises or any part thereof against loss or damage by fire and explosion for the time being subsisting may become void or voidable or whereby the rate of premium thereof may be increased and to repay to the House Owner on demand all sums paid by way of increased premium and all expenses incurred by the House Owner in or about renewal of any such policy rendered necessary by breach or non-observance of this covenant.\n" +
                    "\n" +
                    "(r)\tNot to alter the electricity and/or water meters to the Demised Premises and/or to commit any unlawful act in and around the Demised Premises.\n" +
                    "\n" +
                    "(s)\tTo adopt every reasonable and foreseeable precaution necessary to prevent any acts of vandalism in or to the Demised Premises and any part thereof.\n" +
                    "\n" +
                    "(t)\tTo keep the House Owner indemnified of all actions causes of action including third party actions, liabilities, claims, demands, costs and damages, losses and expenses of every kind whatsoever which may arise in consequence of any breach of covenant or neglect in the performance of this Tenancy by the negligence of the Tenant, its servants or agents or invitees or licensees.\n" +
                    "\n" +
                    "(u)\tTo obtain all requisite licenses and permits and comply with all laws, by-laws, rules and regulations affecting the Demised Premises or its usage or the Tenant or occupier thereof which are now in force or which may hereafter be enacted (if applicable).\n" +
                    "\n" +
                    "(v)\tThe Tenant may exhibit its business signage or signboard of such size and dimension in or at such location as approved by the House Owner and where necessary, as approved by the Local Authority.\n" +
                    "\n" +
                    "\n" +
                    "5.\tHouse Owner’s Covenants\n" +
                    "\n" +
                    "5.1\tThe House Owner hereby covenants with the Tenant as follows:-\n" +
                    "\n" +
                    "(a)\tThat the Tenant observing and performing all its covenants herein contained shall peacefully and quietly hold and enjoy the Demised Premises for the Term free from any interruption by the House Owner or anyone claiming through or under them.\n" +
                    "\n" +
                    "(b)\tThe House Owner shall pay all quit rents and assessments in respect of the Demised Premises.\n" +
                    "\n" +
                    "(c)\tUpon expiry of the Term, to allow the Tenant to remove all the fixtures put in by the Tenant provided that the removal thereof will not cause damage to the Demised Premises and provided further that Clause 4.1(l) is complied with by the Tenant.\n" +
                    "\n" +
                    "(d)\tThat the House Owner shall insure and keep insured the whole of the Demised Premises against fire, lighting, explosion, storm, tempest and other catastrophes provided that the House Owner shall not be obliged to insure any fixtures, fittings, goods, effects and stock-in-trade belonging to the Tenant and shall not be liable for any loss or damage in respect thereof.\n" +
                    "\n" +
                    "\n" +
                    "6.\tMutual Covenants\n" +
                    "\n" +
                    "6.1\tPROVIDED ALWAYS AND IT IS EXPRESSLY AGREED as follows:-\n" +
                    "\n" +
                    "(a)\tIf the rent hereby reserved is in arrears (whether legally demanded in writing or not) for seven (7) days next after the day on which the same ought to have been paid or in the case of the breach or non-observance or non-performance of any of the terms herein contained on the part of the Tenant to be kept, done or performed, and/or the Tenant shall be petitioned for winding up (whether voluntarily or otherwise) or a receiver is appointed or enter into composition or arrangement with its creditors and/or the assets of the Tenant on the Demised Premises become subject to seizure by any third party for satisfaction of debt, it shall be lawful for the House Owner at any time thereafter to retake possession of the Demised Premises and this Tenancy shall absolutely determine and the Security Deposit shall be forfeited in favour of the House Owner but without prejudice to the right of action of the House Owner in respect of any breach by the Tenant of the terms and conditions herein contained.\n" +
                    "\n" +
                    "(b)\tAny notice required to be given to or to be served on the House Owner or the Tenant shall be in writing and shall be deemed to be sufficiently served on the House Owner or the Tenant if sent by registered post to the House Owner or the Tenant at the abovementioned address.\n" +
                    "\n" +
                    "(c)\tIt is hereby mutually agreed by both parties that the House Owner shall be responsible for the application of electricity supply, meter and payment of deposit with Sarawak Energy Berhad whereas the Tenant shall be responsible for the application of water supply, meter and payment of deposit with Kuching Water Board.\n" +
                    "\n" +
                    "(d)\tThis Tenancy Agreement shall be binding on and enure for the benefit of the successors, permitted assigns and representatives of the House Owner and the Tenant.\n" +
                    "\n" +
                    "(e)\tAcceptance of rent by the House Owner shall not be deemed to operate as a waiver by the House Owner of any right to proceed against the Tenant in respect of a breach of any of the terms or obligations herein contained.\n" +
                    "\n" +
                    "(f)\tThe parties hereto agree that the Tenant and any person/party claiming under or through the Tenant shall not and is not entitled to lodge any caveat over the Property and/or the Demised Premises under or by virtue of or pursuant to this Tenancy Agreement.\n" +
                    "\n" +
                    "(g)\tAll legal fees, stamp duties and other disbursements in relation to the negotiation, preparation, execution and perfection (where applicable) of this Tenancy Agreement shall be borne by the parties hereto in equal share of one-half (1/2) each.\n" +
                    "\n" +
                    "\n" +
                    "7.\tEarly Termination\n" +
                    "\n" +
                    "7.1\tIn the event that the House Owner shall wish to prematurely terminate this Tenancy, the House Owner shall give to the Tenant three (3) months’ prior written notice.\n" +
                    "\n" +
                    "7.2\tIn the event that the Tenant shall wish to prematurely terminate this Tenancy, the Tenant shall give to the House Owner three (3) months’ prior written notice.\n" +
                    "\n" +
                    "7.3\tThe Tenant shall continue to punctually pay the Rental and observe and perform all the terms and covenants herein contained until vacant possession of the Demised Premises is delivered up to the House Owner.\n" +
                    "\n" +
                    "\n" +
                    "8.\tOption to Renew\n" +
                    "\n" +
                    "8.1\tThe House Owner hereby grants to the Tenant an option to renew this Tenancy for a further term as stated in Section 5(ii) of the Schedule annexed hereto if it has:-\n" +
                    "\n" +
                    "(a)\tpaid the rent reserved by this Tenancy and observed and performed its covenants contained or implied in it, up to the expiration of the present Term; and\n" +
                    "\n" +
                    "(b)\tgiven the House Owner written notice of its wish for a renewal not less than three (3) months before that expiration.\n" +
                    "\n" +
                    "8.2\tIn the case of renewal of this Tenancy Agreement under Clause 8.1:-\n" +
                    "\n" +
                    "(a)\tthe rental shall be subject to negotiation between the parties, at the prevailing market rate at the time of renewal as stated in Section 5(ii) of the Schedule annexed hereto; and\n" +
                    "\n" +
                    "(b)\tthe covenants and conditions shall otherwise be the same as those in this Tenancy Agreement excepting this present covenant for renewal.\n" +
                    "\n" +
                    "\n" +
                    "9.\tAnnexed Schedule\n" +
                    "\n" +
                    "9.1\tThe terms stated in the Schedule annexed hereto shall be incorporated into and be deemed to be part and parcel of this Agreement.\n" +
                    "\n" +
                    "\n" +
                    "10.\tSales and Services Tax\n" +
                    "\n" +
                    "10.1\tFor any imposition now or hereafter by the Government of Malaysia or the relevant authorities of the Sales and Services Tax (hereinafter referred to as \"SST\") or any relevant taxes of similar nature pursuant to the taxation policy or legislation in force in Malaysia on the Rental, the Tenant shall solely bear and pay the SST and all such taxes in accordance to the time, manner and mode under the latest law, regulations and/or guidelines.\n" +
                    "\n" +
                    "\n" +
                    "11.\tEnforceability\n" +
                    "\n" +
                    "11.1\tNotwithstanding this Tenancy Agreement is not in the statutory form of a Memorandum of Sublease and is not registered under the provisions of the Sarawak Land Code (Cap.81), the parties hereby agree and declare that the tenure, terms and conditions herein shall be fully binding and enforceable by the parties hereto.\n";
            //</editor-fold>
            house.setAgreement(agreement);
            System.out.println(house.getAgreement());
            house.setImageUrl("no");
            house.setMainImageUrl("no");
            house.setOwner_name(user.getUsername());
            house.setHouseOwnerId(user.getId());
            house.setPhone(user.getPhone());
            house.setStatus("Waiting For Info");
            houseService.addNewHouse(house);
            info.setFlag(true);

        } else {
            info.setFlag(false);
            info.setErrorMsg("Please Login First!");
        }


        ObjectMapper mapper = new ObjectMapper();

        //response json to client
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), info);
    }

    /**
     * multifile upload
     *
     * @return
     */
//    @PostMapping(value = "/uploadImg")
//    public void uploadImg(@RequestParam("id")Integer id, @RequestPart("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        User user = null;
//        ResultInfo info = new ResultInfo();
//        try {
//            user = (User)request.getSession().getAttribute("user");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        House houseSearch = houseService.findHouseById(id);
//        int houseOwnerId = houseSearch.getHouseOwnerId();
//
//
//        System.out.println(user.getId());
//        System.out.println(houseOwnerId);
//        if (user!=null){
//
//            if (houseOwnerId == user.getId()){
//
//                String property = System.getProperty("user.dir");
//                String path = property+"\\src\\main\\resources\\static\\image\\";
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < files.length; i++) {
//                    String kind = files[i].getOriginalFilename().split("\\.")[1];
//                    String realPath = path+new Date().getTime()+"."+kind;
//                    files[i].transferTo(new File(realPath));
//                    if (i < files.length-1){
//                        String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
//                        stringBuilder.append(absolutePath+",");
//                    }else{
//                        String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
//                        stringBuilder.append(absolutePath);
//                    }
//                }
//                System.out.println(new String(stringBuilder));
//                houseService.updateHouse(id,new String(stringBuilder));
//                info.setFlag(true);
//            } else {
//                info.setFlag(false);
//                info.setErrorMsg("You can only edit your house!");
//            }
//        } else {
//            info.setFlag(false);
//            info.setErrorMsg("Please Log in !");
//        }
//        commonUtils.writeValue(info,response);
//    }
    @RequestMapping("/uploadImg")
    public void uploadImg(@RequestParam("id") Integer id, @RequestPart("files") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        House houseSearch = houseService.findHouseById(id);
        int houseOwnerId = houseSearch.getHouseOwnerId();


        System.out.println(user.getId());
        System.out.println(houseOwnerId);
        if (user != null) {

            if (houseOwnerId == user.getId()) {

                String property = System.getProperty("user.dir");
                String path = property + "\\src\\main\\resources\\static\\image\\";
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    String kind = files[i].getOriginalFilename().split("\\.")[1];
                    String realPath = path + new Date().getTime() + "." + kind;
                    files[i].transferTo(new File(realPath));
                    if (i < files.length - 1) {
                        String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
                        stringBuilder.append(absolutePath + ",");
                    } else {
                        String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
                        stringBuilder.append(absolutePath);
                    }
                }
                System.out.println(new String(stringBuilder));
                houseService.updateHouse(id, new String(stringBuilder));
                info.setFlag(true);
            } else {
                info.setFlag(false);
                info.setErrorMsg("You can only edit your house!");
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("Please Log in !");
        }
        commonUtils.writeValue(info, response);
    }


    @RequestMapping("/uploadMainImg")
    public void uploadMainImg(@RequestParam("id") Integer id, @RequestPart("file") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = null;
        ResultInfo info = new ResultInfo();
        try {
            user = (User) request.getSession().getAttribute("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        House houseSearch = houseService.findHouseById(id);
        int houseOwnerId = houseSearch.getHouseOwnerId();

        if (user != null) {

            if (houseOwnerId == user.getId()) {

                String property = System.getProperty("user.dir");
                String path = property + "\\src\\main\\resources\\static\\image\\";
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    String kind = files[i].getOriginalFilename().split("\\.")[1];
                    String realPath = path + new Date().getTime() + "." + kind;
                    files[i].transferTo(new File(realPath));
                    if (i < files.length - 1) {
                        String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
                        stringBuilder.append(absolutePath + ",");
                    } else {
                        String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
                        stringBuilder.append(absolutePath);
                    }
                }
                System.out.println(new String(stringBuilder));
                houseService.updateHouseMainImg(id, new String(stringBuilder));
                info.setFlag(true);
            } else {
                info.setFlag(false);
                info.setErrorMsg("You can only edit your house!");
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("Please Log in !");
        }
        commonUtils.writeValue(info, response);
    }

    @RequestMapping("/uploadBillImg")
    public void uploadBillImg(@RequestParam("id") Integer id, @RequestPart("file") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {


        String property = System.getProperty("user.dir");
        String path = property + "\\src\\main\\resources\\static\\image\\";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < files.length; i++) {
            String kind = files[i].getOriginalFilename().split("\\.")[1];
            String realPath = path + new Date().getTime() + "." + kind;
            files[i].transferTo(new File(realPath));
            if (i < files.length - 1) {
                String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
                stringBuilder.append(absolutePath + ",");
            } else {
                String absolutePath = realPath.substring(realPath.lastIndexOf("i"));
                stringBuilder.append(absolutePath);
            }
        }
        System.out.println(new String(stringBuilder));
        orderService.updateBillImg(id, new String(stringBuilder));
        commonUtils.writeValue(true,response);

    }
}
