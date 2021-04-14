package com.example.botanicallibrary.en;

public class Local {
    public  static final String BOTANICALS="Botanicals",
            RANK="rank",
            NAMEDEFAULT="default",
            PARENTKEY="parentKey",
            IMAGEBG="imageBg",
            SPECIES="SPECIES",GENUS="GENUS",FAMILY="FAMILY",ORDER="ORDER",PHYLUM="PHYLUM",CLASS="CLASS",NAME="name",
            IMGBG="imgBackground",
            QUESTION="Question";
    public static  String FOMATDATE="yyyy:MM:dd_HH:mm:ss";
    public  static final int REQUEST_CODE_GET_IMAGE=1,
            REQUEST_CODE_GET_DATA=10,
            REQUEST_CODE_CAMERA=51   ,
            REQUEST_CODE_GALLERY=19,
            LIMIT=50;



    public static class BundleLocal{
        public static String ISSIGNIN="ISSIGNIN", KEY="key", NAME="Name",
        EMAIL="email",PATHIMAGE="pathImage",
        WIDTH="width",HEIGHT="height",QUALITY="quality",RESPONSEREALIZE="responseRealize";
    }
    public static class firebaseLocal{
        public static String BOTANICALS="Botanicals",
                ANSWERS="Answers",
                KEY="key",
                NAME="Name",
                IMAGE="image",IMAGEBG="imageBg",
                AMOUNT="Amount", KEYAREA="KeyArea",NUMBER="Number",
                DESCRIPTION="Description", LABLE="lable",DATE="date",USER="User",QUESTIONS="Question",
                NUMBERORDER="NumberOrder",
                CONTENT="Content",DIRIMAGE="images/",
                CONTRIBUTE="Contributes";
    }
    public static class DeviceLocal{
        public static  final  String NAMEDATADEVICE="Bo_Login",
                EMAIL="Bo_EmailLogin",
                PASSWORD="Bo_PasswordLogin",
                ID="id";
    }
}
