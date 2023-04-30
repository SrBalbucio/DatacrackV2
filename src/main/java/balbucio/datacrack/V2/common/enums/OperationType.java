package balbucio.datacrack.V2.common.enums;

public enum OperationType {

    GETDATAPACK("read.datapack"){
        @Override
        public String getPermCheck(String path){
            return getPerm()+"."+path;
        }
    }, PUTDATAPACK("write.datapack") {
        @Override
        public String getPermCheck(String path) {
            return getPerm()+"."+path;
        }
    }, EXISTSDATAPACK("info.datapack") {
        @Override
        public String getPermCheck(String path) {
            return getPerm()+"."+path;
        }
    }, DELETEDATAPACK("delete.datapack") {
        @Override
        public String getPermCheck(String path) {
            return getPerm();
        }
    };

    private String perm;
    OperationType(String perm){
        this.perm = perm;

    }

    public String getPerm(){
        return perm;
    }

    public abstract String getPermCheck(String path);
}
