package com.cw.wizbank.fm;

class FMDateStatus {
    private int id = 0;
    private String status = null;
    private String title = null;
    private String slotLabel = null;
    private String slot1 = null;
    private String slot2 = null;
    private String slot3 = null;

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getStatus() {
        return this.status;
    }

    public String getSlotLabel() {
        return this.slotLabel;
    }

    public String getSlot1() {
        return this.slot1;
    }

    public String getSlot2() {
        return this.slot2;
    }

    public String getSlot3() {
        return this.slot3;
    }

    public void setId(int pId) {
        this.id = pId;
    }

    public void setTitle(String pTitle) {
        this.title = pTitle;
    }

    public void setStatus(String pStatus) {
        this.status = pStatus;
    }

    public void setSlotLabel(String pSlotLabel) {
        this.slotLabel = pSlotLabel;
    }

    public void setSlot1(String pSlot1) {
        this.slot1 = pSlot1;
    }

    public void setSlot2(String pSlot2) {
        this.slot2 = pSlot2;
    }

    public void setSlot3(String pSlot3) {
        this.slot3 = pSlot3;
    }
}