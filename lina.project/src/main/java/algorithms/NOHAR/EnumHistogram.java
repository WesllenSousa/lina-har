
package algorithms.NOHAR;

/**
 *
 * @author wesllen
 */
public enum EnumHistogram {
    
    EQUAL(0, "EQUAL"),
    INSIDE(1, "INSIDE"),
    OUTSIDE(2, "OUTSIDE"),
    SLACK(3, "SLACK");
    
    private Integer ordem;
    private String descricao;
    
    private EnumHistogram(Integer ordem, String descricao){
        this.ordem = ordem;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    @Override
    public String toString() {
        return this.descricao;
    }
    
    public static EnumHistogram parse(int ordem) {  
         EnumHistogram enumn = null;   
         for (EnumHistogram item : EnumHistogram.values()) {  
             if (item.getOrdem() == ordem) {  
                 enumn = item;  
                 break;  
             }  
         }  
         return enumn;  
     }
    
}
