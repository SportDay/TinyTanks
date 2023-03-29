package fr.mardiH.model;

public class CaseImgNameType {

    private final String imgPath;
    private final String text;
    private final char type;

    public CaseImgNameType(String imgPath, String text, char type) {
        this.imgPath = imgPath;
        this.text = text;
        this.type = type;
    }

    public static CaseImgNameType parse(String type) {
        if (type != null) {
            if (type.contains(";")) {
                String[] strType = type.split(";");
                if (strType.length == 3) {
                    return new CaseImgNameType(strType[0], strType[1], strType[2].charAt(0));
                }
            }
        }
        return null;
    }

    public String getCaseText() {
        return text;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getType() {
        return String.valueOf(type);
    }

    @Override
    public String toString() {
        return imgPath + ";" + text + ";" + type;
    }
}
