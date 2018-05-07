package com.laf.manager.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.MemberDao;
import com.laf.manager.dto.Member;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.dto.Shop;
import com.laf.manager.dto.Ticket;
import com.laf.manager.querycondition.ticket.PointsGotCondition;
import com.laf.manager.querycondition.ticket.TicketFilterCondition;
import com.laf.manager.querycondition.ticket.TicketSaveCondition;
import com.laf.manager.service.MallService;
import com.laf.manager.service.ShopService;
import com.laf.manager.service.TicketService;
import com.laf.manager.utils.file.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class TicketController {

    @Autowired
    MemberDao memberDao;

    @Autowired
    MallService mallService;

    @Autowired
    ShopService shopService;

    @Autowired
    TicketService ticketService;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    FileProperties fileProperties;

    @GetMapping("/tickets")
    public ModelAndView getTickets(HttpSession session) {

        ModelAndView model = new ModelAndView();

        TicketFilterCondition condition = null;

        if (session.getAttribute("tickets_queryCondition") != null) {
            condition = (TicketFilterCondition) session.getAttribute("tickets_queryCondition");
        } else {
            condition = new TicketFilterCondition();
        }

        int total = ticketService.getMultipleTicketsCount(condition);
        Map<String, Object> pageMap = new HashMap<String, Object>();
        pageMap.put("index", condition.getPage());
        pageMap.put("size", condition.getSize());
        pageMap.put("total", total);

        model.getModel().put("pageMap", pageMap);

        List<Ticket> list = ticketService.getTicketList(condition);

        model.getModel().put("tickets", list);
        model.setViewName("tickets");

        List<PlaneMap> maps = shopService.getShopsGroupByMap();
        model.getModel().put("maps", maps);

        return model;
    }

    @GetMapping("/ticket")
    public ModelAndView getTicket(@RequestParam Integer ticketId, @RequestParam Integer memberId) {
        ModelAndView model = new ModelAndView();

        if (ticketId == 0 || memberId == 0) {
            model.setViewName("404");
            return model;
        }

        int mallId = settingsProperties.getMallId();
        Member member = memberDao.getMemberById(memberId, mallId);
        Ticket ticket = ticketService.getTicketDetail(ticketId);

        if (member == null || ticket == null) {
            model.setViewName("404");
            return model;
        }

//        List<PlaneMap> planeMaps = mallService.getPlaneMapList();

//        ShopQueryCondition condition = new ShopQueryCondition();
//        condition.setMall_id(mallId);
//        condition.setMap_id(planeMaps.get(0).getMap_id());
//        List<Shop> shops = shopService.getShopList(condition);
        List<Shop> shops = shopService.getShopAllList();

        Map<String, String> memberMap = new HashMap<>();
        memberMap.put("mobile", member.getMobile());
        memberMap.put("name", member.getName());
        memberMap.put("sex", member.getSex() == 0 ? "男" : "女");
        memberMap.put("address", member.getAddress());
        memberMap.put("usablePoints", member.getUsable_points() + "");
        memberMap.put("cumulatePoints", member.getCumulate_points() + "");
        memberMap.put("birthday", member.getBirthday() + "");
        memberMap.put("levelId", member.getLevel_id() + "");
        memberMap.put("memberId", member.getMember_id() + "");
        memberMap.put("cumulateAmount", member.getCumulate_amount() + "");

        String occupation = "";
        switch (member.getOccupation()) {
            case 0:
                occupation = "教师";
                break;
            case 1:
                occupation = "会计";
                break;
            case 2:
                occupation = "IT";
                break;
            case 3:
                occupation = "金融";
                break;
            case 4:
                occupation = "销售";
                break;
            case 5:
                occupation = "营业员";
                break;
            case 6:
                occupation = "公务员";
                break;
            case 7:
                occupation = "自由职业";
                break;
        }

        memberMap.put("occupation", occupation);

        List<PlaneMap> maps = shopService.getShopsGroupByMap();
        model.getModel().put("maps", maps);

        model.getModel().put("member", memberMap);
//        model.getModel().put("planeMaps", planeMaps);
        model.getModel().put("shops", shops);
        model.getModel().put("ticketId", ticketId);
        model.getModel().put("ticketPicture", "/ticketImage?imageUrl=" + ticket.getFile_url());
        model.getModel().put("ticket", ticket);

        model.setViewName("handleticket");
        return model;
    }

    @GetMapping("/ticketView")
    public ModelAndView ticketView(@RequestParam Integer ticketId, @RequestParam Integer memberId) {
        ModelAndView model = new ModelAndView();

        if (ticketId == 0 || memberId == 0) {
            model.setViewName("404");
            return model;
        }

        int mallId = settingsProperties.getMallId();
        Member member = memberDao.getMemberById(memberId, mallId);
        Ticket ticket = ticketService.getTicketDetail(ticketId);

        if (member == null || ticket == null) {
            model.setViewName("404");
            return model;
        }

        Map<String, String> memberMap = new HashMap<>();
        memberMap.put("mobile", member.getMobile());
        memberMap.put("name", member.getName());
        memberMap.put("sex", member.getSex() == 0 ? "男" : "女");
        memberMap.put("address", member.getAddress());
        memberMap.put("usablePoints", member.getUsable_points() + "");
        memberMap.put("cumulatePoints", member.getCumulate_points() + "");
        memberMap.put("birthday", member.getBirthday() + "");
        memberMap.put("levelId", member.getLevel_id() + "");
        memberMap.put("memberId", member.getMember_id() + "");
        memberMap.put("cumulateAmount", member.getCumulate_amount() + "");

        String occupation = "";
        switch (member.getOccupation()) {
            case 0:
                occupation = "教师";
                break;
            case 1:
                occupation = "会计";
                break;
            case 2:
                occupation = "IT";
                break;
            case 3:
                occupation = "金融";
                break;
            case 4:
                occupation = "销售";
                break;
            case 5:
                occupation = "营业员";
                break;
            case 6:
                occupation = "公务员";
                break;
            case 7:
                occupation = "自由职业";
                break;
        }

        memberMap.put("occupation", occupation);
        model.getModel().put("member", memberMap);
        model.getModel().put("ticket", ticket);
        model.getModel().put("ticketPicture", "/ticketImage?imageUrl=" + ticket.getFile_url());

        model.setViewName("ticketview");
        return model;
    }

    @PostMapping("/saveTicket")
    public String editTicket(TicketSaveCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = 0;

        try {
            result = ticketService.operateTicket(condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result > 0) {
            return "redirect:tickets";
        } else {
            return "redirect:error";
        }
    }

    @GetMapping("/delticket")
    public String delTicket(@RequestParam Integer ticketId) {
        int result = ticketService.removeTicketById(ticketId);
        if (result <= 0) return "redirect error";
        return "redirect tickets";
    }

    @PostMapping("/ticket/getpoints")
    public ResponseEntity getPointsFromTicket(PointsGotCondition condition, BindingResult errors) {
        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        int points = ticketService.getPoints(condition);

        return new ResponseEntity<Integer>(points, HttpStatus.OK);
    }


    private String handlePostJsonParamError(BindingResult errors) {
        String errMsg = "[";

        for (ObjectError error : errors.getAllErrors()) {
            errMsg += error.getDefaultMessage() + ",";
        }

        errMsg += "]";

        return errMsg;
    }

    @GetMapping("/handlePoints")
    public ModelAndView getHandlePoints() {
        ModelAndView model = new ModelAndView();
        List<Shop> shops = shopService.getShopAllList();

        model.getModel().put("shops", shops);
        model.setViewName("handlepoints");
        return model;
    }

    @PostMapping("/saveHandlePoints")
    public String editHandlePoints(TicketSaveCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = 0;

        try {
            result = ticketService.operateHandlePoints(condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result > 0) {
            return "redirect:tickets";
        } else {
            return "redirect:error";
        }
    }

    @PostMapping("/tickets/filter")
    public String filterTickets(TicketFilterCondition condition, BindingResult errors, String filterJson, HttpSession session) {
        session.setAttribute("tickets_queryCondition", condition);
        session.setAttribute("tickets_filterJson", filterJson);

        return "redirect:/tickets";
    }

    @PostMapping("/tickets/reset")
    public String resetTickets(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.startsWith("tickets_")) {
                session.removeAttribute(name);
            }
        }

        return "redirect:/tickets";
    }

    @GetMapping("/ticketImage")
    public void getImage(@RequestParam String imageUrl, HttpServletResponse response)
            throws IOException, ImageProcessingException {

        String suffixName = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
        String $url = fileProperties.getDomain() + imageUrl;
        URL url = new URL($url);
        URLConnection urlConnection = url.openConnection();

        Integer turn = 360;

        try (InputStream is = urlConnection.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            ExifIFD0Directory db = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            int orientation = db == null ? 1 : db.getInt(ExifIFD0Directory.TAG_ORIENTATION);

            //确定旋转度数
            if (orientation == 0 || orientation == 1) {
                turn = 360;
            } else if (orientation == 3) {
                turn = 180;
            } else if (orientation == 6) {
                turn = 90;
            } else if (orientation == 8) {
                turn = 270;
            }
        } catch (MetadataException ex) {
            turn = 0;
        }

        BufferedImage src = ImageIO.read(url);

        if (turn != 0) {
            BufferedImage des = RotateImage.Rotate(src, turn);
            ImageIO.write(des, suffixName, response.getOutputStream());
        } else {
            ImageIO.write(src, suffixName, response.getOutputStream());
        }
    }
}

class RotateImage {

    public static BufferedImage Rotate(Image src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }
}
