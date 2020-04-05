package club.simplecreate.pojo;

public class Type {
    private Integer typeId;

    private String typeContent;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent == null ? null : typeContent.trim();
    }
}