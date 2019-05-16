package com.wofang.demo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 房源列表数据
 *
 * @author chenyacheng
 * @date 2019/03/06
 */
public class HouseListResult {

    /**
     * pageCount : 1
     * lastPage : true
     * prePage : 0
     * pageIndexKey : pageWrapper.pageIndex
     * maxPageSize : 5000
     * maxRowCount : 0
     * jsonString : null
     * pageIndexKeyStr : pageWrapper.pageIndexStr
     * pageSize : 10
     * paramEncoding : utf-8
     * list : [{"distId":"6410","star":"高档型","distance":6.597606011034601,"origPrice":"0","hotelId":2707,"isTag":"0","labels":null,"lastPriceDate":1566715301000,"productImage":"","providerId":null,"minPrice":328,"name":"海口我度假国际酒店（原倚能戴斯酒店）","theme":"1","averagePrice":null,"introduction":"海口我度假国际酒店（原海口倚能戴斯酒店），由我房旅居集团旗下海南我度假酒店公寓管理有限公司运营管理，酒位于海口国兴CBD与海口中央生活区交界处，与国兴片区仅一桥之隔，","buyRate":5,"status":"3"},{"distId":"6410","star":"五星级","distance":10.159713668829943,"origPrice":0,"hotelId":2327,"isTag":"0","labels":"可做饭","lastPriceDate":1566715301000,"productImage":"hotel/2327/manage/cover/cover.jpg","providerId":null,"minPrice":599,"name":"海口观澜湖温泉酒店","theme":"1","averagePrice":null,"introduction":"海口观澜湖温泉酒店位于海口龙华区观澜湖大道1号，近冯小刚电影公社, 泰迪熊博物馆, 海洋世界, 观澜湖新城, 距美兰机场及市区仅15分钟车程。","buyRate":0,"status":"3"},{"distId":"6410","star":"高档型","distance":3.4869435476417485,"origPrice":0,"hotelId":2341,"isTag":"1","labels":"可做饭,自住入驻,落地阳台,海景房","lastPriceDate":1566715301000,"productImage":"hotel/2341/manage/cover/cover.jpg","providerId":null,"minPrice":11,"name":"测试皇马酒店-购买无效","theme":"8","averagePrice":null,"introduction":"几点几分看见看见看见房间号集合家电家具分解机旷达科技福晶科技看风景 酒店金风科技金风科技付款里IE姐姐结婚登记科技愧疚的拒绝公开动物福建省可解决房价开始开始的积极复健科付款担惊受恐借记卡","buyRate":0,"status":"3"}]
     * params :
     * rowOffset : 0
     * nextpage : 1
     * s : {"bedroom":null,"lowPrice":null,"highPrice":null,"checkInDate":"","leavingDate":"","qtime":0,"typeKey":null,"kind":null,"id":null,"identify":null,"itinNo":null,"userId":0,"roleId":0,"beginDate":null,"endDate":null,"beginTime":null,"endTime":null,"strBeginTime":null,"strEndTime":null,"beginCode":null,"endCode":"6410","star":null,"type":null,"key":null,"priceTop":null,"priceEnd":null,"name":null,"mobile":null,"multi":0,"count":0,"day":1,"feature":-1,"orderK":"5","orderV":null,"status":"3","address":null,"param1":null,"param2":null,"param3":null,"param4":null,"param5":null,"param6":null,"param7":null,"param8":null,"param9":null,"param10":null,"source":null,"locationSwitch":"off","point":null,"lat":0,"lng":0,"userlat":20.007756,"userlng":110.344244,"latSize":0.00984,"lngSize":0.00895,"includeSelf":404,"model":{}}
     * firstPage : true
     * pageIndex : 1
     * pageIndexStr : null
     * rowCount : 3
     */

    private String pageCount;
    private Boolean lastPage;
    private String prePage;
    private String pageIndexKey;
    private String maxPageSize;
    private Integer maxRowCount;
    private Object jsonString;
    private String pageIndexKeyStr;
    private String pageSize;
    private String paramEncoding;
    private String params;
    private String rowOffset;
    private String nextpage;
    private SBean s;
    private Boolean firstPage;
    private String pageIndex;
    private Object pageIndexStr;
    private Integer rowCount;
    private List<ListBean> list;

    public String getPageCount() {
        return pageCount == null ? "" : pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public Boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public String getPrePage() {
        return prePage == null ? "" : prePage;
    }

    public void setPrePage(String prePage) {
        this.prePage = prePage;
    }

    public String getPageIndexKey() {
        return pageIndexKey == null ? "" : pageIndexKey;
    }

    public void setPageIndexKey(String pageIndexKey) {
        this.pageIndexKey = pageIndexKey;
    }

    public String getMaxPageSize() {
        return maxPageSize == null ? "" : maxPageSize;
    }

    public void setMaxPageSize(String maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public Integer getMaxRowCount() {
        return maxRowCount;
    }

    public void setMaxRowCount(Integer maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public Object getJsonString() {
        return jsonString;
    }

    public void setJsonString(Object jsonString) {
        this.jsonString = jsonString;
    }

    public String getPageIndexKeyStr() {
        return pageIndexKeyStr == null ? "" : pageIndexKeyStr;
    }

    public void setPageIndexKeyStr(String pageIndexKeyStr) {
        this.pageIndexKeyStr = pageIndexKeyStr;
    }

    public String getPageSize() {
        return pageSize == null ? "" : pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getParamEncoding() {
        return paramEncoding == null ? "" : paramEncoding;
    }

    public void setParamEncoding(String paramEncoding) {
        this.paramEncoding = paramEncoding;
    }

    public String getParams() {
        return params == null ? "" : params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRowOffset() {
        return rowOffset == null ? "" : rowOffset;
    }

    public void setRowOffset(String rowOffset) {
        this.rowOffset = rowOffset;
    }

    public String getNextpage() {
        return nextpage == null ? "" : nextpage;
    }

    public void setNextpage(String nextpage) {
        this.nextpage = nextpage;
    }

    public SBean getS() {
        return s;
    }

    public void setS(SBean s) {
        this.s = s;
    }

    public Boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        this.firstPage = firstPage;
    }

    public String getPageIndex() {
        return pageIndex == null ? "" : pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Object getPageIndexStr() {
        return pageIndexStr;
    }

    public void setPageIndexStr(Object pageIndexStr) {
        this.pageIndexStr = pageIndexStr;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public List<ListBean> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class SBean {
        /**
         * bedroom : null
         * lowPrice : null
         * highPrice : null
         * checkInDate :
         * leavingDate :
         * qtime : 0
         * typeKey : null
         * kind : null
         * id : null
         * identify : null
         * itinNo : null
         * userId : 0
         * roleId : 0
         * beginDate : null
         * endDate : null
         * beginTime : null
         * endTime : null
         * strBeginTime : null
         * strEndTime : null
         * beginCode : null
         * endCode : 6410
         * star : null
         * type : null
         * key : null
         * priceTop : null
         * priceEnd : null
         * name : null
         * mobile : null
         * multi : 0
         * count : 0
         * day : 1
         * feature : -1
         * orderK : 5
         * orderV : null
         * status : 3
         * address : null
         * param1 : null
         * param2 : null
         * param3 : null
         * param4 : null
         * param5 : null
         * param6 : null
         * param7 : null
         * param8 : null
         * param9 : null
         * param10 : null
         * source : null
         * locationSwitch : off
         * point : null
         * lat : 0.0
         * lng : 0.0
         * userlat : 20.007756
         * userlng : 110.344244
         * latSize : 0.00984
         * lngSize : 0.00895
         * includeSelf : 404
         * model : {}
         */

        private Object bedroom;
        private Object lowPrice;
        private Object highPrice;
        private String checkInDate;
        private String leavingDate;
        private int qtime;
        private Object typeKey;
        private Object kind;
        private Object id;
        private Object identify;
        private Object itinNo;
        private int userId;
        private int roleId;
        private Object beginDate;
        private Object endDate;
        private Object beginTime;
        private Object endTime;
        private Object strBeginTime;
        private Object strEndTime;
        private Object beginCode;
        private String endCode;
        private Object star;
        private Object type;
        private Object key;
        private Object priceTop;
        private Object priceEnd;
        private Object name;
        private Object mobile;
        private int multi;
        private int count;
        private int day;
        private int feature;
        private String orderK;
        private Object orderV;
        private String status;
        private Object address;
        private Object param1;
        private Object param2;
        private Object param3;
        private Object param4;
        private Object param5;
        private Object param6;
        private Object param7;
        private Object param8;
        private Object param9;
        private Object param10;
        private Object source;
        private String locationSwitch;
        private Object point;
        private double lat;
        private double lng;
        private double userlat;
        private double userlng;
        private double latSize;
        private double lngSize;
        private int includeSelf;
        private ModelBean model;

        public Object getBedroom() {
            return bedroom;
        }

        public void setBedroom(Object bedroom) {
            this.bedroom = bedroom;
        }

        public Object getLowPrice() {
            return lowPrice;
        }

        public void setLowPrice(Object lowPrice) {
            this.lowPrice = lowPrice;
        }

        public Object getHighPrice() {
            return highPrice;
        }

        public void setHighPrice(Object highPrice) {
            this.highPrice = highPrice;
        }

        public String getCheckInDate() {
            return checkInDate;
        }

        public void setCheckInDate(String checkInDate) {
            this.checkInDate = checkInDate;
        }

        public String getLeavingDate() {
            return leavingDate;
        }

        public void setLeavingDate(String leavingDate) {
            this.leavingDate = leavingDate;
        }

        public int getQtime() {
            return qtime;
        }

        public void setQtime(int qtime) {
            this.qtime = qtime;
        }

        public Object getTypeKey() {
            return typeKey;
        }

        public void setTypeKey(Object typeKey) {
            this.typeKey = typeKey;
        }

        public Object getKind() {
            return kind;
        }

        public void setKind(Object kind) {
            this.kind = kind;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getIdentify() {
            return identify;
        }

        public void setIdentify(Object identify) {
            this.identify = identify;
        }

        public Object getItinNo() {
            return itinNo;
        }

        public void setItinNo(Object itinNo) {
            this.itinNo = itinNo;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public Object getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(Object beginDate) {
            this.beginDate = beginDate;
        }

        public Object getEndDate() {
            return endDate;
        }

        public void setEndDate(Object endDate) {
            this.endDate = endDate;
        }

        public Object getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Object beginTime) {
            this.beginTime = beginTime;
        }

        public Object getEndTime() {
            return endTime;
        }

        public void setEndTime(Object endTime) {
            this.endTime = endTime;
        }

        public Object getStrBeginTime() {
            return strBeginTime;
        }

        public void setStrBeginTime(Object strBeginTime) {
            this.strBeginTime = strBeginTime;
        }

        public Object getStrEndTime() {
            return strEndTime;
        }

        public void setStrEndTime(Object strEndTime) {
            this.strEndTime = strEndTime;
        }

        public Object getBeginCode() {
            return beginCode;
        }

        public void setBeginCode(Object beginCode) {
            this.beginCode = beginCode;
        }

        public String getEndCode() {
            return endCode;
        }

        public void setEndCode(String endCode) {
            this.endCode = endCode;
        }

        public Object getStar() {
            return star;
        }

        public void setStar(Object star) {
            this.star = star;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Object getPriceTop() {
            return priceTop;
        }

        public void setPriceTop(Object priceTop) {
            this.priceTop = priceTop;
        }

        public Object getPriceEnd() {
            return priceEnd;
        }

        public void setPriceEnd(Object priceEnd) {
            this.priceEnd = priceEnd;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public int getMulti() {
            return multi;
        }

        public void setMulti(int multi) {
            this.multi = multi;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getFeature() {
            return feature;
        }

        public void setFeature(int feature) {
            this.feature = feature;
        }

        public String getOrderK() {
            return orderK;
        }

        public void setOrderK(String orderK) {
            this.orderK = orderK;
        }

        public Object getOrderV() {
            return orderV;
        }

        public void setOrderV(Object orderV) {
            this.orderV = orderV;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public Object getParam1() {
            return param1;
        }

        public void setParam1(Object param1) {
            this.param1 = param1;
        }

        public Object getParam2() {
            return param2;
        }

        public void setParam2(Object param2) {
            this.param2 = param2;
        }

        public Object getParam3() {
            return param3;
        }

        public void setParam3(Object param3) {
            this.param3 = param3;
        }

        public Object getParam4() {
            return param4;
        }

        public void setParam4(Object param4) {
            this.param4 = param4;
        }

        public Object getParam5() {
            return param5;
        }

        public void setParam5(Object param5) {
            this.param5 = param5;
        }

        public Object getParam6() {
            return param6;
        }

        public void setParam6(Object param6) {
            this.param6 = param6;
        }

        public Object getParam7() {
            return param7;
        }

        public void setParam7(Object param7) {
            this.param7 = param7;
        }

        public Object getParam8() {
            return param8;
        }

        public void setParam8(Object param8) {
            this.param8 = param8;
        }

        public Object getParam9() {
            return param9;
        }

        public void setParam9(Object param9) {
            this.param9 = param9;
        }

        public Object getParam10() {
            return param10;
        }

        public void setParam10(Object param10) {
            this.param10 = param10;
        }

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public String getLocationSwitch() {
            return locationSwitch;
        }

        public void setLocationSwitch(String locationSwitch) {
            this.locationSwitch = locationSwitch;
        }

        public Object getPoint() {
            return point;
        }

        public void setPoint(Object point) {
            this.point = point;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getUserlat() {
            return userlat;
        }

        public void setUserlat(double userlat) {
            this.userlat = userlat;
        }

        public double getUserlng() {
            return userlng;
        }

        public void setUserlng(double userlng) {
            this.userlng = userlng;
        }

        public double getLatSize() {
            return latSize;
        }

        public void setLatSize(double latSize) {
            this.latSize = latSize;
        }

        public double getLngSize() {
            return lngSize;
        }

        public void setLngSize(double lngSize) {
            this.lngSize = lngSize;
        }

        public int getIncludeSelf() {
            return includeSelf;
        }

        public void setIncludeSelf(int includeSelf) {
            this.includeSelf = includeSelf;
        }

        public ModelBean getModel() {
            return model;
        }

        public void setModel(ModelBean model) {
            this.model = model;
        }

        public static class ModelBean {
        }
    }

    public static class ListBean implements Serializable {
        /**
         * distId : 6410
         * star : 高档型
         * distance : 6.597606011034601
         * origPrice : 0
         * hotelId : 2707
         * isTag : 0
         * labels : null
         * lastPriceDate : 1566715301000
         * productImage :
         * providerId : null
         * minPrice : 328.0
         * name : 海口我度假国际酒店（原倚能戴斯酒店）
         * theme : 1
         * averagePrice : null
         * introduction : 海口我度假国际酒店（原海口倚能戴斯酒店），由我房旅居集团旗下海南我度假酒店公寓管理有限公司运营管理，酒位于海口国兴CBD与海口中央生活区交界处，与国兴片区仅一桥之隔，
         * buyRate : 5
         * status : 3
         */

        private String distId;
        private String star;
        private double distance;
        private String origPrice;
        private int livingRoom;
        private int suitableNumber;
        private Integer hotelId;
        private String isTag;
        private int bedroom;
        private String labels;
        private Long lastPriceDate;
        private String productImage;
        private Date providerId;
        private double minPrice;
        private String name;
        private String theme;
        private Double averagePrice;
        private String introduction;
        private int buyRate;
        private String status;

        public String getDistId() {
            return distId == null ? "" : distId;
        }

        public void setDistId(String distId) {
            this.distId = distId;
        }

        public String getStar() {
            return star == null ? "" : star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getOrigPrice() {
            return origPrice == null ? "" : origPrice;
        }

        public void setOrigPrice(String origPrice) {
            this.origPrice = origPrice;
        }

        public int getLivingRoom() {
            return livingRoom;
        }

        public void setLivingRoom(int livingRoom) {
            this.livingRoom = livingRoom;
        }

        public int getSuitableNumber() {
            return suitableNumber;
        }

        public void setSuitableNumber(int suitableNumber) {
            this.suitableNumber = suitableNumber;
        }

        public Integer getHotelId() {
            return hotelId;
        }

        public void setHotelId(Integer hotelId) {
            this.hotelId = hotelId;
        }

        public String getIsTag() {
            return isTag == null ? "" : isTag;
        }

        public void setIsTag(String isTag) {
            this.isTag = isTag;
        }

        public int getBedroom() {
            return bedroom;
        }

        public void setBedroom(int bedroom) {
            this.bedroom = bedroom;
        }

        public String getLabels() {
            return labels == null ? "" : labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }

        public Long getLastPriceDate() {
            return lastPriceDate;
        }

        public void setLastPriceDate(Long lastPriceDate) {
            this.lastPriceDate = lastPriceDate;
        }

        public String getProductImage() {
            return productImage == null ? "" : productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public Date getProviderId() {
            return providerId;
        }

        public void setProviderId(Date providerId) {
            this.providerId = providerId;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTheme() {
            return theme == null ? "" : theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public Double getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(Double averagePrice) {
            this.averagePrice = averagePrice;
        }

        public String getIntroduction() {
            return introduction == null ? "" : introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getBuyRate() {
            return buyRate;
        }

        public void setBuyRate(int buyRate) {
            this.buyRate = buyRate;
        }

        public String getStatus() {
            return status == null ? "" : status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
