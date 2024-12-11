package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Message;
import com.example.demo.model.Reply;
import com.example.demo.model.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import com.example.demo.service.WeatherService;
import com.example.demo.util.HttpUtils;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    // MessageController.java


    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us"; // 返回about-us.html页面的名称（不包括.html扩展名）
    }
    @GetMapping("/weather-today")
    public String weatherToday(Model model) throws Exception {
        String host = "https://ali-weather.showapi.com";
        String path = "/area-to-weather";
        String method = "GET";
        String appcode = "9eff6851ff5d4767890bc23c9d659f5d";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("area", "青岛");  // 城市为青岛

        // 发送HTTP请求获取响应
        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        String responseBody = EntityUtils.toString(response.getEntity());

        // 解析JSON响应
        JSONObject jsonResponse = JSONObject.parseObject(responseBody);
        JSONObject showapi_res_body = jsonResponse.getJSONObject("showapi_res_body");
        JSONObject now = showapi_res_body.getJSONObject("now");

        // 提取天气信息
        String temperature = now.getString("temperature");  // 当前温度
        String weather = now.getString("weather");  // 当前天气
        String windDirection = now.getString("wind_direction");  // 风向
        String windPower = now.getString("wind_power");  // 风力
        String aqi = now.getString("aqi");  // 空气质量指数

        // 将天气信息传递给模型
        model.addAttribute("temperature", temperature);
        model.addAttribute("weather", weather);
        model.addAttribute("windDirection", windDirection);
        model.addAttribute("windPower", windPower);
        model.addAttribute("aqi", aqi);

        // 返回前端页面
        return "weather-today";  // 视图名称
    }

    /*
    { "showapi_res_error": "" | "showapi_res_id": "6744207dfb638c7d8463597b" | "showapi_res_code": 0 | "showapi_fee_num": 1 | "showapi_res_body": {"list":[{"area":"青岛" | "distric":"青岛" | "areaCode":"370200" | "cityInfo":{"c4":"qingdao" | "c17":"+8" | "c5":"青岛" | "c6":"shandong" | "latitude":36.088 | "c7":"山东" | "c10":"2" | "c12":"266000" | "c2":"qingdao" | "c15":"75" | "c11":"0532" | "c1":"101120201" | "longitude":120.343 | "c3":"青岛" | "c9":"中国" | "c16":"AZ9532" | "c8":"china"} | "areaid":"101120201" | "flag":true | "prov":"山东"} | {"area":"胶州" | "distric":"青岛" | "areaCode":"370281" | "cityInfo":{"c4":"qingdao" | "c15":"16" | "c5":"青岛" | "c6":"shandong" | "latitude":36.252 | "c7":"山东" | "c10":"3" | "c8":"china" | "c2":"jiaozhou" | "c12":"266300" | "c11":"0532" | "c1":"101120205" | "c3":"胶州" | "c9":"中国" | "c17":"+8" | "c16":"AZ9532" | "longitude":119.925} | "areaid":"101120205" | "flag":true | "prov":"山东"} | {"area":"莱西" | "distric":"青岛" | "areaCode":"370285" | "cityInfo":{"c4":"qingdao" | "c17":"+8" | "c5":"青岛" | "c6":"shandong" | "latitude":36.855 | "c7":"山东" | "c10":"3" | "c8":"china" | "c2":"laixi" | "c11":"0532" | "c1":"101120207" | "c12":"266600" | "longitude":120.435 | "c3":"莱西" | "c9":"中国" | "c16":"AZ9532" | "c15":"52"} | "areaid":"101120207" | "flag":true | "prov":"山东"} | {"area":"崂山" | "distric":"青岛" | "areaCode":"370212" | "cityInfo":{"c4":"qingdao" | "c15":"44" | "c5":"青岛" | "c6":"shandong" | "latitude":36.1 | "c7":"山东" | "c10":"3" | "c8":"china" | "c2":"laoshan" | "longitude":120.25 | "c1":"101120202" | "c11":"0532" | "c17":"+8" | "c9":"中国" | "c12":"266100" | "c16":"AZ9532" | "c3":"崂山"} | "areaid":"101120202" | "flag":true | "prov":"山东"} | {"area":"城阳" | "distric":"青岛" | "areaCode":"370214" | "cityInfo":{"c4":"qingdao" | "c11":"0532" | "c5":"青岛" | "c6":"shandong" | "latitude":36.307 | "c7":"山东" | "c1":"101120211" | "c12":"266200" | "c2":"chengyang" | "c17":"+8" | "c8":"china" | "c10":"3" | "longitude":120.391 | "c9":"中国" | "c15":"12" | "c16":"AZ9532" | "c3":"城阳"} | "areaid":"101120211" | "flag":true | "prov":"山东"} | {"area":"即墨" | "distric":"青岛" | "areaCode":"370282" | "cityInfo":{"c4":"qingdao" | "c15":"30" | "longitude":120.555 | "c6":"shandong" | "latitude":36.452 | "c7":"山东" | "c10":"3" | "c12":"266200" | "c2":"jimo" | "c5":"青岛" | "c11":"0532" | "c9":"中国" | "c17":"+8" | "c3":"即墨" | "c1":"101120204" | "c16":"AZ9532" | "c8":"china"} | "areaid":"101120204" | "flag":true | "prov":"山东"} | {"area":"市北" | "distric":"青岛" | "areaCode":"370203" | "cityInfo":{"c4":"qingdao" | "c11":"0532" | "c5":"青岛" | "c6":"shandong" | "latitude":36.087 | "c7":"山东" | "c1":"101120209" | "c12":"266100" | "c2":"shibei" | "c17":"+8" | "c8":"china" | "c10":"3" | "longitude":120.369 | "c9":"中国" | "c15":"27" | "c16":"AZ9532" | "c3":"市北"} | "areaid":"101120209" | "flag":true | "prov":"山东"} | {"area":"李沧" | "distric":"青岛" | "areaCode":"370213" | "cityInfo":{"c4":"qingdao" | "c11":"0532" | "c5":"青岛" | "c6":"shandong" | "latitude":36.144 | "c7":"山东" | "c1":"101120210" | "c12":"266100" | "c2":"licang" | "c17":"+8" | "c8":"china" | "c10":"3" | "longitude":120.427 | "c9":"中国" | "c15":"40" | "c16":"AZ9532" | "c3":"李沧"} | "areaid":"101120210" | "flag":true | "prov":"山东"} | {"area":"平度" | "areaCode":"370283" | "cityInfo":{"c4":"qingdao" | "c11":"0532" | "longitude":119.919 | "c6":"shandong" | "latitude":36.764 | "c7":"山东" | "c1":"101120208" | "c12":"266700" | "c2":"pingdu" | "c5":"青岛" | "c17":"+8" | "c10":"3" | "c9":"中国" | "c3":"平度" | "c8":"china" | "c16":"AZ9532" | "c15":"52"} | "areaid":"101120208" | "flag":true | "distric":"青岛" | "prov":"山东"} | {"area":"黄岛" | "areaCode":"370211" | "cityInfo":{"c4":"qingdao" | "c15":"13" | "longitude":119.907 | "c6":"shandong" | "latitude":35.863 | "c7":"山东" | "c1":"101120206" | "c8":"china" | "c2":"huangdao" | "c5":"青岛" | "c9":"中国" | "c10":"3" | "c12":"266400" | "c3":"黄岛" | "c17":"+8" | "c16":"AZ9532" | "c11":"0532"} | "areaid":"101120206" | "flag":true | "distric":"青岛" | "prov":"山东"} | {"area":"市南" | "distric":"青岛" | "areaCode":"370202" | "cityInfo":{"c4":"qingdao" | "c11":"0532" | "c5":"青岛" | "c6":"shandong" | "latitude":36.074 | "c7":"山东" | "c1":"101120203" | "c12":"266100" | "c2":"shinan" | "c17":"+8" | "c8":"china" | "c10":"3" | "longitude":120.407 | "c9":"中国" | "c15":"49" | "c16":"AZ9532" | "c3":"市南"} | "areaid":"101120203" | "flag":true | "prov":"山东"}] | "remark":"" | "ret_code":0} }
    */

    @GetMapping("/list")
    public String listMessages(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String timeFrame,
                               @RequestParam(required = false, defaultValue = "desc") String sortByReplies,
                               Model model, HttpSession session) {
        List<Message> messages;

        // 确保返回类型是 List<Message>
        if (category != null && !category.isEmpty()) {
            messages = messageService.getMessagesByCategory(category);
        } else {
            messages = messageService.getAllMessages();
        }

        if (timeFrame != null && !timeFrame.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime;

            switch (timeFrame) {
                case "1h":
                    startTime = now.minusHours(1);
                    break;
                case "1d":
                    startTime = now.minusDays(1);
                    break;
                case "1w":
                    startTime = now.minusWeeks(1);
                    break;
                case "1m":
                    startTime = now.minusMonths(1);
                    break;
                default:
                    startTime = now;
            }

            messages.removeIf(message -> message.getCreatedAt().isBefore(startTime));
        }

        Map<Long, Integer> repliesCountMap = messageService.getMessageRepliesCountMap();

        // 确保此处 message 是 Message 类型
        messages.forEach(message -> {
            message.setRepliesCount(repliesCountMap.getOrDefault(message.getId(), 0));
        });

        boolean desc = "desc".equalsIgnoreCase(sortByReplies);
        if (desc) {
            messages.sort(Comparator.comparingInt(Message::getRepliesCount).reversed());
        } else {
            messages.sort(Comparator.comparingInt(Message::getRepliesCount));
        }
        Map<Long, Integer> repliesCount = messageService.getMessageRepliesCount();
        model.addAttribute("messages", messages);
        model.addAttribute("categories", Message.getCategories());
        model.addAttribute("repliesCount", repliesCount);
        model.addAttribute("sortByReplies", sortByReplies);
        return "list";
    }



    // MessageController.java
    @GetMapping("/detail/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {
        Message message = messageService.getMessageById(id);
        if (message != null) {
            List<Reply> replies = messageService.getRepliesByMessageId(id);
            model.addAttribute("replies", replies); // 添加回复到模型
            model.addAttribute("message", message);
            return "detail";  // 返回显示详情的模板
        }
        return "redirect:/message/list";  // 如果消息不存在，重定向到消息列表
    }


    // 展示添加留言页面
    @GetMapping("/add")
    public String showAddMessagePage(Model model) {
        model.addAttribute("message", new Message());  // 确保模型属性传递给模板
        return "add";
    }

    // 处理添加留言
    @PostMapping("/add")
    public String addMessage(@ModelAttribute Message message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            message.setUserId(user.getId());
            message.setCreatedAt(user.getCreatedAt());
            message.setUser(user);
            System.out.println(message.getUserId());
            System.out.println(message.getUser());
            messageService.saveMessage(message);  // 保存Message对象
            return "redirect:/message/list";
        }
        return "redirect:/user/login";
    }

    // 显示编辑留言页面
    @GetMapping("/edit/{id}")
    public String showEditMessagePage(@PathVariable Long id, Model model) {
        Message message = (Message)messageService.getMessageById(id);  // 确保类型正确
        if (message != null) {
            model.addAttribute("message", message);
            return "edit";
        }
        return "redirect:/message/list";
    }

    // 编辑留言
    @PostMapping("/edit/{id}")
    public String editMessage(@PathVariable Long id, @ModelAttribute Message message) {
        message.setId(id);
        messageService.updateMessage(message);  // 更新Message对象
        return "redirect:/message/list";
    }

    // 删除留言
    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return "redirect:/message/list";
    }

    // 搜索留言
    @GetMapping("/search")
    public String searchMessages(@RequestParam String keyword, Model model) {
        List<Message> messages = messageService.searchMessagesByKeyword(keyword);  // 确保类型正确
        model.addAttribute("messages", messages);
        return "list";
    }


    // MessageController.java
    @GetMapping("/top-today")
    public String topTodayMessages(Model model) {
        List<Message> messages = messageService.getTopTodayMessages();
        model.addAttribute("messages", messages);
        model.addAttribute("categories", Message.getCategories());
        return "top-today"; // 返回新模板的名称
    }
}
