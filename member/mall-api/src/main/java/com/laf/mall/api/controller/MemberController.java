package com.laf.mall.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.activity.ActivityQueryCondition;
import com.laf.mall.api.querycondition.activity.MyActivityQueryCondition;
import com.laf.mall.api.querycondition.assessment.AssessmentListCondition;
import com.laf.mall.api.querycondition.assessment.AssessmentSaveCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponListQueryCondition;
import com.laf.mall.api.querycondition.coupon.MyCouponQueryCondition;
import com.laf.mall.api.querycondition.member.*;
import com.laf.mall.api.querycondition.points.PointsPayCashCondition;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.querycondition.ticket.TicketQueryCondition;
import com.laf.mall.api.querycondition.ticket.TicketUploadCondition;
import com.laf.mall.api.querycondition.validcode.ValidCodeCheckCondition;
import com.laf.mall.api.querycondition.validcode.ValidCodeRequestCondition;
import com.laf.mall.api.service.*;
import com.laf.mall.api.utils.file.FileProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.core.support.SimpleResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    FileProperties fileProperties;

    @Autowired
    PointsService pointsService;

    @Autowired
    ActivityService activityService;

    @Autowired
    CouponService couponService;

    @GetMapping("/{memberId:\\d+}")
    @JsonView(Member.MemberAppView.class)
    @ApiOperation(value = "获取会员信息接口")
    public ResponseEntity getInfo(@PathVariable Integer memberId, @RequestParam Integer mallId) {

        MemberQueryCondition condition = new MemberQueryCondition();
        condition.setMemberId(memberId);
        condition.setMallId(mallId);

        Member member = memberService.getMemberById(condition);

        if (member == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }

    @GetMapping("/m")
    @JsonView(Member.MemberAppView.class)
    @ApiOperation(value = "获取会员信息接口(手机)")
    public ResponseEntity getInfoByMobile(@RequestParam String mobile) {

        Member member = memberService.getMemberByMobile(mobile);

        if (member == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }

    @GetMapping("/baseInfo")
    @JsonView(Member.MemberAppView.class)
    @ApiOperation(value = "获取会员基本信息接口(手机)")
    public ResponseEntity getBaseInfoByMobile(@RequestParam String mobile) {

        Member member = memberService.getBaseInfoByMobile(mobile);

        if (member == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }

    @GetMapping
    @JsonView(Member.MemberAppView.class)
    @ApiOperation(value = "获取会员信息接口(微信公众号appID,用户openId)")
    public ResponseEntity getInfoByOpenIdInMall(@RequestParam String appId, @RequestParam String openId) {

        Member member = memberService.getMemberByOpenIdInMall(appId, openId);

        if (member == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "会员注册接口")
    public ResponseEntity regist(@Valid @RequestBody MemberRegistCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        int result = memberService.registMember(condition);

        if (result == -1) {
            return new ResponseEntity(new SimpleResponse(-1, "手机号已被绑定"), HttpStatus.BAD_REQUEST);
        }

        if (result == 0) {
            return new ResponseEntity(new SimpleResponse(0, "请求参数错误"), HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.getSimpleMemberByMobile(condition.getMobile());
        return new ResponseEntity<Member>(member, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value = "会员信息修改接口")
    public ResponseEntity<SimpleResponse> editInfo(@Valid @RequestBody MemberUpdateCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = errors.getAllErrors().get(0).getDefaultMessage();

            return new ResponseEntity<SimpleResponse>(new SimpleResponse(msg), HttpStatus.BAD_REQUEST);
        }

        int result = 0;

        try {
            result = memberService.editMemberInfo(condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 0) {
            return new ResponseEntity<SimpleResponse>(new SimpleResponse(result, "更新失败。"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<SimpleResponse>(new SimpleResponse("更新成功。"), HttpStatus.OK);
    }

    @PostMapping("/vcode")
    @ApiOperation(value = "获取验证码接口")
    public ResponseEntity getValidCode(@Valid @RequestBody ValidCodeRequestCondition condition) throws Exception {

        if (memberService.sendSmsVerificationCode(condition.getMobile(), condition.getMallId()) == 1) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/vcodeCheck")
    @ApiOperation(value = "验证验证码接口")
    public ResponseEntity<SimpleResponse> checkValidCode(@Valid @RequestBody ValidCodeCheckCondition condition) throws Exception {
        int result = memberService.checkValidCode(condition.getMobile(), condition.getVcode());

        if (result == -1) {
            return new ResponseEntity<SimpleResponse>(new SimpleResponse(-1, "手机号已存在。"), HttpStatus.BAD_REQUEST);
        }

        if (result == 0) {
            return new ResponseEntity<SimpleResponse>(new SimpleResponse(-1, "验证码验证失败。"), HttpStatus.GONE);
        }

        return new ResponseEntity<SimpleResponse>(new SimpleResponse("验证成功。"), HttpStatus.OK);
    }

    @PostMapping("/uploadTicket/{memberId:\\d+}")
    @ApiOperation(value = "小票上传接口")
    public ResponseEntity upload(MultipartFile file, @PathVariable Integer memberId, @Valid TicketUploadCondition condition) throws IOException {

        if (file == null || !file.getName().equals("file")) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        String uploadFileName = file.getOriginalFilename();
        String suffixName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
        log.info("=========suffixName == {} ============", suffixName);

        String folder = fileProperties.getPath();
        String fileName = memberId + "_" + new Date().getTime() + suffixName;

        File localFile = new File(folder, fileName);
        file.transferTo(localFile);

        int result = 0;
        try {
            result = memberService.uploadTicket(fileProperties.getBase() + fileName, memberId, condition);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 0) return new ResponseEntity(HttpStatus.FORBIDDEN);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/ticket/{ticketId:\\d+}")
    @ApiOperation(value = "删除小票接口")
    public ResponseEntity deleteTicket(@PathVariable Integer ticketId) {
        int result = memberService.removeTicket(ticketId);

        if (result == 0) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{memberId:\\d+}/tickets")
    @JsonView(Ticket.TicketAppView.class)
    @ApiOperation(value = "获取会员的小票列表接口")
    public ResponseEntity getTicketList(@PathVariable Integer memberId,
                                        @RequestParam Integer mallId,
                                        @PageableDefault(page = 1, size = 5, sort = "upload_date", direction = Sort.Direction.DESC) Pageable pageable) {

        TicketQueryCondition condition = new TicketQueryCondition(memberId, mallId);
        condition.setPage(pageable.getPageNumber());
        condition.setSize(pageable.getPageSize());
        condition.setOrderBy(pageable.getSort().toString().replace(":", ""));

        List<Ticket> tickets = memberService.getTicketListByMe(condition);

        if (tickets.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
    }

    @PostMapping("/registServices")
    @ApiOperation(value = "商场工作人员注册接口")
    public ResponseEntity registServices(@Valid @RequestBody ServicesRegistCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        int result = memberService.registServices(condition);

        if (result <= 0) return new ResponseEntity("不可重复注册", HttpStatus.BAD_REQUEST);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/registClerk")
    @ApiOperation(value = "核销人员注册接口")
    public ResponseEntity registClerk(@Valid @RequestBody ClerkRegistCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        int result = memberService.registClerk(condition);

        if (result <= 0) return new ResponseEntity("不可重复注册", HttpStatus.BAD_REQUEST);

        return new ResponseEntity(HttpStatus.CREATED);
    }

//    @GetMapping("{mplogId:\\d+}")
//    @JsonView(Points.PointsAppView.class)
//    @ApiOperation(value = "获取积分明细接口")
//    public ResponseEntity getPointsDetail(@PathVariable String mplogId) {
//        Points points = pointsService.getPointsDetail(mplogId);
//
//        if (points == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
//
//        return new ResponseEntity<Points>(points, HttpStatus.OK);
//    }

    @PostMapping("/{memberId:\\d+}/pointsList")
    @JsonView(Points.PointsAppView.class)
    @ApiOperation(value = "获取积分记录接口")
    public ResponseEntity getPointsList(@PathVariable Integer memberId, @Valid @RequestBody PointsQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        List<Points> list = pointsService.getPointsList(condition, memberId);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Points>>(list, HttpStatus.OK);
    }

    @GetMapping("/{memberId}/activityDetail")
    @JsonView(Activity.ActivityAppView.class)
    @ApiOperation(value = "我的活动详情接口")
    public ResponseEntity getActivityDetail(@PathVariable Integer memberId, @RequestParam Integer activityId) {

        Activity activity = activityService.getActivityDetail4Member(activityId, memberId);

        if (activity == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Activity>(activity, HttpStatus.OK);
    }

    @PostMapping("/{memberId:\\d+}/activityList")
    @JsonView(Activity.ActivityListView.class)
    @ApiOperation(value = "我的活动查询接口")
    public ResponseEntity getActivityList(@PathVariable Integer memberId, @Valid @RequestBody MyActivityQueryCondition myActivityQueryCondition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        ActivityQueryCondition condition = new ActivityQueryCondition();
        condition.setMallId(myActivityQueryCondition.getMallId());
        condition.setMemberId(memberId);
        condition.setPage(myActivityQueryCondition.getPage());
        condition.setSize(myActivityQueryCondition.getSize());
        condition.setSort(myActivityQueryCondition.getSort());
        condition.setDirection(myActivityQueryCondition.getDirection());

        List<Activity> list = activityService.getActivityList4Member(condition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Activity>>(list, HttpStatus.OK);
    }

    @PutMapping("/{memberId:\\d+}/activitySignUp/{activityId:\\d+}")
    @ApiOperation(value = "活动报名接口")
    public ResponseEntity activitySignUp(@PathVariable Integer memberId, @PathVariable Integer activityId) {

        int result;
        try {
            result = activityService.signUp(memberId, activityId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result == -1) return new ResponseEntity(new SimpleResponse("活动不存在"), HttpStatus.NOT_FOUND);
        else if (result == -2) return new ResponseEntity(new SimpleResponse("活动已报名"), HttpStatus.GONE);
        else if (result == 0) return new ResponseEntity(new SimpleResponse("活动不可报名或名额已满"), HttpStatus.FORBIDDEN);
        else if (result == -3) return new ResponseEntity(new SimpleResponse("报名失败"), HttpStatus.FORBIDDEN);

        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

    @PutMapping("/{memberId:\\d+}/activitySignIn/{activityId:\\d+}")
    @ApiOperation(value = "活动签到接口")
    public ResponseEntity activitySignIn(@PathVariable Integer memberId, @PathVariable Integer activityId, @RequestParam Integer mallId) {

        if (memberId <= 0 || activityId <= 0 || mallId <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int result;
        try {
            result = activityService.signIn(memberId, activityId, mallId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 4) return new ResponseEntity(result, HttpStatus.FORBIDDEN);
        else return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/{memberId:\\d+}/enableSignIn/{activityId:\\d+}")
    @ApiOperation(value = "限制签到接口")
    public ResponseEntity enableSignIn(@PathVariable Integer memberId, @PathVariable Integer activityId, @RequestParam Integer mallId) {

        if (memberId <= 0 || activityId <= 0 || mallId <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int result = activityService.enableSignIn(memberId, activityId, mallId);

        if (result == 8) return new ResponseEntity(result, HttpStatus.OK);
        else return new ResponseEntity(result, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{memberId:\\d+}/couponList")
    @JsonView(Coupon.CouponAppListView.class)
    @ApiOperation(value = "获取我的券列表接口")
    public ResponseEntity getCouponList(@PathVariable Integer memberId, @Valid @RequestBody MyCouponQueryCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        if (memberId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        MyCouponListQueryCondition myCondition = new MyCouponListQueryCondition();
        myCondition.setCouponStatus(condition.getCouponStatus());
        myCondition.setMallId(condition.getMallId());
        myCondition.setMemberId(memberId);
        myCondition.setPage(condition.getPage() == 0 ? 1 : condition.getPage());
        myCondition.setSize(condition.getSize() == 0 ? 5 : condition.getSize());
        List<Coupon> list = couponService.getCouponListByMember(myCondition);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<List<Coupon>>(list, HttpStatus.OK);
    }

    @GetMapping("/{memberId:\\d+}/receiveCoupon/{couponId:\\d+}")
    @ApiOperation(value = "优惠券领取接口")
    public ResponseEntity receiveCoupon(@PathVariable Integer memberId, @PathVariable Integer couponId) {

        if (memberId <= 0 || couponId <= 0)
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        int result = 0;

        try {
            result = memberService.receiveCouponByMember(memberId, couponId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result == 8) return new ResponseEntity(result, HttpStatus.OK);
        else return new ResponseEntity(result, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/coupon/{crlId:\\d+}")
    @JsonView(Coupon.CouponDetailView.class)
    @ApiOperation(value = "我的券详情接口")
    public ResponseEntity getCoupon(@PathVariable Integer crlId) {

        if (crlId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        Coupon coupon = couponService.getCouponById(crlId);

        if (coupon == null) return new ResponseEntity(new SimpleResponse("券不存在"), HttpStatus.NOT_FOUND);

        return new ResponseEntity(coupon, HttpStatus.OK);
    }

    @GetMapping("/verify")
    @ApiOperation(value = "核销接口")
    public ResponseEntity toVerify(@RequestParam Integer memberId, @RequestParam Integer crlId) {

        if (memberId <= 0 || crlId <= 0) {
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        }

        int result = 0;

        try {
            result = couponService.toVerify(memberId, crlId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result <= 0) return new ResponseEntity(new SimpleResponse("核销失败"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new SimpleResponse("核销成功"), HttpStatus.OK);
    }

    @GetMapping("/verification/{memberId:\\d+}")
    @JsonView(VerificationClerk.VerificationClerkAppInfoView.class)
    @ApiOperation(value = "获取核销人员信息接口")
    public ResponseEntity getVerificationClerkByMember(@PathVariable Integer memberId) {

        if (memberId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        VerificationClerk vc = memberService.getVerificationClerkByMember(memberId);

        if (vc == null) return new ResponseEntity(new SimpleResponse("核销人员不存在"), HttpStatus.NOT_FOUND);

        return new ResponseEntity(vc, HttpStatus.OK);
    }

    @PostMapping("/{memberId:\\d+}/suggest")
    @ApiOperation(value = "提交评价建议接口")
    public ResponseEntity submitAssessment(@PathVariable Integer memberId, @Valid @RequestBody AssessmentSaveCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        if (memberId <= 0) {
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        }

        int result = memberService.submitAssessment(condition, memberId);

        if (result == -1) return new ResponseEntity(new SimpleResponse("商户不存在"), HttpStatus.NOT_FOUND);
        if (result == 0) return new ResponseEntity(new SimpleResponse("会员不存在"), HttpStatus.NOT_FOUND);
        if (result == 1) return new ResponseEntity(new SimpleResponse("提交失败"), HttpStatus.FORBIDDEN);

        return new ResponseEntity(new SimpleResponse("提交成功"), HttpStatus.CREATED);
    }

    @PostMapping("/{memberId:\\d+}/suggestedList")
    @JsonView(Assessment.AssessmentListView.class)
    @ApiOperation(value = "评价建议列表接口")
    public ResponseEntity getAssessmentList(@PathVariable Integer memberId, @Valid @RequestBody AssessmentListCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        if (memberId <= 0) {
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        }

        List<Assessment> list = memberService.getAssessmentList(condition, memberId);

        if (list.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/suggested/{suggestedId:\\d+}")
    @JsonView(Assessment.AssessmentAppView.class)
    @ApiOperation(value = "评价建议详情接口")
    public ResponseEntity getAssessmentInfo(@PathVariable Integer suggestedId) {

        if (suggestedId <= 0) return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);

        Assessment assessment = memberService.getAssessmentInfo(suggestedId);

        if (assessment == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity(assessment, HttpStatus.OK);
    }

    //    @PostMapping("/{memberId:\\d+}/paycash")
//    @ApiOperation(value = "积分抵现接口")
    public ResponseEntity pointsPayCash(@PathVariable Integer memberId, @RequestBody PointsPayCashCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }

        if (memberId <= 0) {
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        }

        int result;
        try {
            result = memberService.pointsPayCash(condition, memberId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result == 0) return new ResponseEntity(new SimpleResponse("会员不存在"), HttpStatus.NOT_FOUND);
        if (result == 1) return new ResponseEntity(new SimpleResponse("商户不存在"), HttpStatus.NOT_FOUND);
        if (result == 2) return new ResponseEntity(new SimpleResponse("抵现规则不存在"), HttpStatus.NOT_FOUND);
        if (result == 3) return new ResponseEntity(new SimpleResponse("积分不足"), HttpStatus.FORBIDDEN);
        if (result == 4) return new ResponseEntity(new SimpleResponse("抵现失败"), HttpStatus.FORBIDDEN);

        return new ResponseEntity(new SimpleResponse("抵现成功"), HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    @ApiOperation(value = "激活接口")
    public ResponseEntity toActivate(@RequestParam Integer memberId, @RequestParam Integer crlId, @RequestParam Integer mallId) {

        if (memberId <= 0 || crlId <= 0 || mallId <= 0) {
            return new ResponseEntity(new SimpleResponse("参数无效"), HttpStatus.BAD_REQUEST);
        }

        int result;

        try {
            result = couponService.toActivate(memberId, crlId, mallId);
        } catch (MallDBException e) {
            result = e.getResult();
        }

        if (result == 0) return new ResponseEntity(new SimpleResponse("非相关工作人员"), HttpStatus.NOT_FOUND);
        if (result == 1) return new ResponseEntity(new SimpleResponse("重复激活"), HttpStatus.FORBIDDEN);
        if (result == 2) return new ResponseEntity(new SimpleResponse("不在有效期内"), HttpStatus.FORBIDDEN);
        if (result == 3) return new ResponseEntity(new SimpleResponse("未到有效激活时间"), HttpStatus.FORBIDDEN);
        if (result == 4) return new ResponseEntity(new SimpleResponse("已超出有效激活数量"), HttpStatus.FORBIDDEN);
        if (result == 6) return new ResponseEntity(new SimpleResponse("激活失败"), HttpStatus.FORBIDDEN);

        return new ResponseEntity(new SimpleResponse("激活成功"), HttpStatus.CREATED);
    }

    @PostMapping("/visit")
    @ApiOperation(value = "会员访问记录接口")
    public ResponseEntity loggingVisitor(@Valid @RequestBody MemberVisitSaveCondition condition, BindingResult errors) {

        if (errors.hasErrors()) {
            String msg = handlePostJsonParamError(errors);

            return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
        }

        int result = memberService.loggingVisitor(condition);

        if (result <= 0) return new ResponseEntity(new SimpleResponse("操作失败"), HttpStatus.FORBIDDEN);

        return new ResponseEntity(new SimpleResponse("操作成功"), HttpStatus.CREATED);

    }

    private String handlePostJsonParamError(BindingResult errors) {
        String errMsg = "[";

        for (ObjectError error : errors.getAllErrors()) {
            errMsg += error.getDefaultMessage() + ",";
        }
        errMsg = errMsg.substring(0, errMsg.length());
        errMsg += "]";

        return errMsg;
    }
}
