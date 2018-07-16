package tcy.api.vo;

public class EvaluationCountVo {
    private int count;
    private String type;

    public EvaluationCountVo(int count,String type){
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
