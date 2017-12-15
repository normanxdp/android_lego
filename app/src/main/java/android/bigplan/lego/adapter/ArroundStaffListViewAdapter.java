package android.bigplan.lego.adapter;

public class ArroundStaffListViewAdapter {
    public enum AroundAdapterType {
        AroundStaff0("/Member/GetAroundStaff", "0") , AroundStaff1("/Member/GetAroundStaff", "1") ;
        private String uri;
        private String type;


        AroundAdapterType(String uri, String type) {
            this.uri = uri;
            this.type = type;
        }

        public String getUri() {
            return uri;
        }
        public String getType(){
            return  type;
        }
        public static AroundAdapterType getExamType(String value, String type) {
                     for (AroundAdapterType examType : AroundAdapterType.values()) {
                            if (value == examType.getUri() && type == examType.getType()) {
                                   return examType;
                              }
                        }
                   return null;
               }
    }
}
