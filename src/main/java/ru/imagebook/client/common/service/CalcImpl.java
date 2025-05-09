package ru.imagebook.client.common.service;

import ru.imagebook.shared.model.*;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.util.MathUtil;

public class CalcImpl implements Calc {
    private static double[] discountPc = new double[10];

    static {
        discountPc[0] = 0;
        discountPc[1] = 3;
        discountPc[2] = 6;
        discountPc[3] = 10;
        discountPc[4] = 14;
        discountPc[5] = 18;
        discountPc[6] = 22;
        discountPc[7] = 26;
        discountPc[8] = 30;
        discountPc[9] = 44;
    }

    private final Order<?> order;
    private final Product product;
    private final Album album;
    private final PricingData data;

    private final int BB, CC, DD, EE, F, G;
    private final double Ккрс;

    private final Double Ца3_1, Ца3_2, Ца3_3;
    private final Double Кламстр_1, Кламстр_2, Кламстр_3;
    private final Double Цфальц_1;
    private final Double Цламоблм_1;
    private final Double Кформмп_1, Кформмп_2, Кформмп_3;
    private final Double Кламобмп_1;
    private final Double Кформпру_1, Кформпру_2, Кформпру_3, Кформпру_4, Кформпру_5;
    private final Double Цпру_1;
    private final Double Кламоб_1;
    private final Double Кформстоб_1, Кформстоб_2, Кформстоб_3, Кформстоб_4, Кформстоб_5;
    private final Double Цстоб_1;
    private final Double Кформплотт_1, Кформплотт_2, Кформплотт_3, Кформплотт_4, Кформплотт_5;
    private final Double Цплотт_1;
    private final Double Кформ_1, Кформ_2, Кформ_3, Кформ_4, Кформ_5;
    private final Double Цкож_1;
    private final Double КИ11, КИ12, КИ21, КИ22, КИ3, КИ4;
    private final int TAPPH, TAPC;

// private final int ПоздравительныйСебестоимость, ПоздравительныйСтоимость;

    public CalcImpl(Order<?> order, PricingData data) {
        this.order = order;
        this.data = data;

        product = order.getProduct();
        album = (Album) product;

        // if (!(product instanceof Album))
        // throw new BaseError("Product not supported: " + product.getClass());

        BB = product.getType();
        CC = product.getNumber();
        DD = order.getPageCount();
        EE = order.getColor().getNumber();
        F = order.getCoverLamination();
        G = order.getPageLamination();

        Ккрс = product.getMultiplicity();

        Ца3_1 = (Double) data.get("Ца3_1");
        Ца3_2 = (Double) data.get("Ца3_2");
        Ца3_3 = (Double) data.get("Ца3_3");

        Кламстр_1 = (Double) data.get("Кламстр_1");
        Кламстр_2 = (Double) data.get("Кламстр_2");
        Кламстр_3 = (Double) data.get("Кламстр_3");

        Цфальц_1 = (Double) data.get("Цфальц_1");

        Цламоблм_1 = (Double) data.get("Цламоблм_1");

        Кформмп_1 = (Double) data.get("Кформмп_1");
        Кформмп_2 = (Double) data.get("Кформмп_2");
        Кформмп_3 = (Double) data.get("Кформмп_3");

        Кламобмп_1 = (Double) data.get("Кламобмп_1");

        Кформпру_1 = (Double) data.get("Кформпру_1");
        Кформпру_2 = (Double) data.get("Кформпру_2");
        Кформпру_3 = (Double) data.get("Кформпру_3");
        Кформпру_4 = (Double) data.get("Кформпру_4");
        Кформпру_5 = (Double) data.get("Кформпру_5");

        Цпру_1 = (Double) data.get("Цпру_1");

        Кламоб_1 = (Double) data.get("Кламоб_1");

        Кформстоб_1 = (Double) data.get("Кформстоб_1");
        Кформстоб_2 = (Double) data.get("Кформстоб_2");
        Кформстоб_3 = (Double) data.get("Кформстоб_3");
        Кформстоб_4 = (Double) data.get("Кформстоб_4");
        Кформстоб_5 = (Double) data.get("Кформстоб_5");

        Цстоб_1 = (Double) data.get("Цстоб_1");

        Кформплотт_1 = (Double) data.get("Кформплотт_1");
        Кформплотт_2 = (Double) data.get("Кформплотт_2");
        Кформплотт_3 = (Double) data.get("Кформплотт_3");
        Кформплотт_4 = (Double) data.get("Кформплотт_4");
        Кформплотт_5 = (Double) data.get("Кформплотт_5");

        Цплотт_1 = (Double) data.get("Цплотт_1");

        Кформ_1 = (Double) data.get("Кформ_1");
        Кформ_2 = (Double) data.get("Кформ_2");
        Кформ_3 = (Double) data.get("Кформ_3");
        Кформ_4 = (Double) data.get("Кформ_4");
        Кформ_5 = (Double) data.get("Кформ_5");

        Цкож_1 = (Double) data.get("Цкож_1");

        КИ11 = (Double) data.get("КИ11");
        КИ12 = (Double) data.get("КИ12");
        КИ21 = (Double) data.get("КИ21");
        КИ22 = (Double) data.get("КИ22");
        КИ3 = (Double) data.get("КИ3");
        КИ4 = (Double) data.get("КИ4");

        TAPPH = data.get("TAPPH") != null ? ((Double) data.get("TAPPH")).intValue() : 0;
        TAPC = data.get("TAPC") != null ? ((Double) data.get("TAPC")).intValue() : 0;

// ПоздравительныйСебестоимость = ((Double) data
// .get("ПоздравительныйСебестоимость")).intValue();
// ПоздравительныйСтоимость = ((Double) data.get("ПоздравительныйСтоимость"))
// .intValue();
    }

    @Override
    public int getPrintingHousePrice() {
        double phBasePrice = album.getPhBasePrice();
        double phPagePrice = album.getPhPagePrice();
        double phCoverLaminationPrice = album.getPhCoverLaminationPrice();
        if (phBasePrice == 0 || phPagePrice == 0) {
            return getOldPrintingHousePrice();
        }

        double pagePrice = phPagePrice;
        if (order.getPageLamination() != PageLamination.NONE) {
            pagePrice += album.getPhPageLaminationPrice();
        }
        double blockPrice = pagePrice * order.getPageCount();

        double phPrice = phBasePrice + blockPrice;
        if (order.getCoverLamination() != CoverLamination.NONE) {
            phPrice += phCoverLaminationPrice;
        }

        phPrice += getFlyleafPhPrice();
        phPrice += getVellumPhPrice();

        return (int) phPrice;
    }

    private int getOldPrintingHousePrice() {
        if (product.isTrialAlbum()) {
            return TAPPH;
        } else {
            double СП = getСП1() + getСП2();
            double Спру = getСпру();
            double Кламоб = getКламоб();
            double Сстоб = getСстоб(Кламоб);
            double Сплотт = getСплотт(Кламоб);
            double Скож = getСкож();

            return (int) (СП + Спру + Сстоб + Сплотт + Скож);
        }
    }

    public double getIB1() {
        double СП1 = getСП1();

        double КИ1;
        if ((BB == 1 || BB == 2 || BB == 3) && (CC == 6 || CC == 10))
            КИ1 = КИ11;
        else if ((BB == 1 || BB == 2 || BB == 3) && CC == 8)
            КИ1 = КИ12;
        else
            КИ1 = КИ4;

        double ib1 = СП1 * КИ1;

        return ib1;
    }

    @Override
    public double getFlyleafPrice(Flyleaf flyleaf) {
        double price = 0;

        if (album.isFlyleafs()) {
            if (flyleaf != null && !flyleaf.getId().equals(Flyleaf.DEFAULT_ID)) {
                price = flyleaf.getPrice() * getFactor();
            }
        }

        return price;
    }

    private double getFlyleafPhPrice() {
        double price = 0;

        if (album.isFlyleafs()) {
            Flyleaf flyleaf = order.getFlyleaf();
            if (flyleaf != null && !flyleaf.getId().equals(Flyleaf.DEFAULT_ID)) {
                price = flyleaf.getPhPrice() *  getFactor();
            }
        }

        return price;
    }

    @Override
    public double getVellumPrice(Vellum vellum) {
        double price = 0;

        if (album.isSupportsVellum() && vellum != null) {
            price = vellum.getPrice() * getFactor();
        }

        return price;
    }

    private double getVellumPhPrice() {
        double price = 0;

        if (album.isSupportsVellum()) {
            Vellum vellum = order.getVellum();
            if (vellum != null) {
                price = vellum.getPhPrice() *  getFactor();
            }
        }

        return price;
    }

    private double getFactor() {
        double factor = 1;
        if (CC == 1 || CC == 2)
            factor = 1;
        else if (CC == 3)
            factor = 1.4;
        else if (CC == 4 || CC == 5)
            factor = 1.75;
        else if (CC == 6 || CC == 7)
            factor = 2;
        else if (CC == 8)
            factor = 2.8;
        else if (CC == 9 || CC == 10)
            factor = 4;
        return factor;
    }


    public double getIB2() {
        double Кламоб = getКламоб();
        double Сплотт = getСплотт(Кламоб);
        double Скож = getСкож();

        double КИ2;
        if (BB == 2 || BB == 5)
            КИ2 = КИ21;
        else
            КИ2 = КИ22;

        return (Сплотт + Скож) * КИ2;
    }

    public double getIB3() {
        double СП2 = getСП2();
        return СП2 * КИ3;
    }

    public double getIB4() {
        double Спру = getСпру();
        double Кламоб = getКламоб();
        double Сстоб = getСстоб(Кламоб);
        return (Спру + Сстоб) * КИ4;
    }

    @Override
    public int getIBX(int quantity, int level) {
        int k;
        double cost;
        boolean specialLevel = false;

        if (quantity == 1)
            k = 1;
        else if (quantity == 2)
            k = 2;
        else if (quantity >= 3 && quantity <= 5)
            k = 3;
        else if (quantity >= 6 && quantity <= 10)
            k = 4;
        else if (quantity >= 11 && quantity <= 20)
            k = 5;
        else if (quantity >= 21 && quantity <= 40)
            k = 6;
        else if (quantity >= 41 && quantity <= 60)
            k = 7;
        else if (quantity >= 61 && quantity <= 100)
            k = 8;
        else
            k = 9;

        boolean oldPrice = false;
        User user = order.getUser();
        if (user != null && user.isOldPrice())
            oldPrice = true;

        Album album = (Album) product;

        // New price (pickbook)
        if (!oldPrice && album.getPagePrice() != 0 && album.getBasePrice() != 0) {
            int level_k = level + 1;
            if (level_k > k)
                k = level_k;

            cost = getAlbumPrice();

            double discountP = discountPc[k - 1];
            double discountSum = Math.ceil(cost * discountP / 100);
            cost = cost - discountSum;

            return (int) Math.round(cost) * quantity;
        }
        // Old price (imagebook)
        else {
            double IB1 = getIB1();
            double IB2 = getIB2();
            double IB3 = getIB3();
            double IB4 = getIB4();

            if (level == 9) {
                specialLevel = true;
                level = 8;
            }
            int level_k = level + 1;
            if (level_k > k)
                k = level_k;

            double IB_k;
            if (BB == 1 || BB == 2)
                IB_k = (Double) data.get("IB_EVERFLAT_" + k);
            else
                IB_k = (Double) data.get("IB_" + k);

            cost = IB1 * (1.0d - IB_k / 100.0d) + IB2 + IB3 + IB4;

            if (specialLevel) {
                if (BB == 1 || BB == 2)
                    cost = cost * (1.0d - 0.16d); // -16%
                else
                    cost = cost * (1.0d - 0.20d); // -20%
            }

            return MathUtil.round10(cost) * quantity;
        }
    }

    double getAlbumPrice() {
        Album album = (Album) product;

        double pagePrice = album.getPagePrice();
        if (order.getPageLamination() != PageLamination.NONE) {
            pagePrice += album.getPageLaminationPrice();
        }
        double blockPrice = pagePrice * order.getPageCount();

        double price = blockPrice + album.getBasePrice();
        // Цена ламинации обложки
        // http://jira.minogin.ru/browse/IMAGEBOOK-223
        if (album.isClipType()
                && order.getCoverLamination() != CoverLamination.NONE
                && album.getCoverLaminationPrice() != 0) {
            price += album.getCoverLaminationPrice();
        }

        price += getFlyleafPrice(order.getFlyleaf());
        price += getVellumPrice(order.getVellum());

        return price;
    }

    @Override
    public int getCostWithoutDiscount(int quantity) {
        return (int) Math.round(getAlbumPrice()) * quantity;
    }

    // Temporary method, for calculating cost with old price
    // Duplicated code
    @Override
    public int getIBXOldPrice(int quantity, int level) {
        int k;
        double cost;
        boolean specialLevel = false;

        if (quantity == 1)
            k = 1;
        else if (quantity == 2)
            k = 2;
        else if (quantity >= 3 && quantity <= 5)
            k = 3;
        else if (quantity >= 6 && quantity <= 10)
            k = 4;
        else if (quantity >= 11 && quantity <= 20)
            k = 5;
        else if (quantity >= 21 && quantity <= 40)
            k = 6;
        else if (quantity >= 41 && quantity <= 60)
            k = 7;
        else if (quantity >= 61 && quantity <= 100)
            k = 8;
        else
            k = 9;

        // Old price
        double IB1 = getIB1();
        double IB2 = getIB2();
        double IB3 = getIB3();
        double IB4 = getIB4();

        if (level == 9) {
            specialLevel = true;
            level = 8;
        }
        int level_k = level + 1;
        if (level_k > k)
            k = level_k;

        double IB_k;
        if (BB == 1 || BB == 2)
            IB_k = (Double) data.get("IB_EVERFLAT_" + k);
        else
            IB_k = (Double) data.get("IB_" + k);

        cost = IB1 * (1.0d - IB_k / 100.0d) + IB2 + IB3 + IB4;

        if (specialLevel) {
            if (BB == 1 || BB == 2)
                cost = cost * (1.0d - 0.16d); // -16%
            else
                cost = cost * (1.0d - 0.20d); // -20%
        }

        return MathUtil.round10(cost) * quantity;
    }

    @Override
    public int getImagebookPrice() {
        if (product.isTrialAlbum()) {
            return order.isTrial() ? 0 : TAPC;
        } else {
            return getIBX(1, 0);
        }
    }

    // public int getImagebookCost2(int level, int discountPc) {
    // Integer quantity = order.getQuantity();
    // if (CC == Product.TRIAL_ALBUM_NUMBER)
    // return getImagebookPrice() * quantity;
    //
    // if (order.getLevel() != null && order.getLevel() > level)
    // level = order.getLevel();
    //
    // if (order.getDiscountPc() > discountPc)
    // discountPc = order.getDiscountPc();
    //
    // int cost = getIBX(quantity, level);
    //
    // double baseCost = getImagebookPrice() * quantity;
    // double discountDouble = discountPc;
    // int discountedCost = (int) ((baseCost * (100d - discountDouble)) / 100d);
    //
    // if (order.getDiscountSum() != null)
    // discountedCost -= order.getDiscountSum();
    //
    // if (discountedCost < 0)
    // discountedCost = 0;
    //
    // return Math.min(cost, discountedCost);
    // }

    private double getКламоб() {
        if (F == 0)
            return 0;
        else if (F == 1)
            return 1;
        else
            return Кламоб_1;
    }

    private double getЦа3() {
        if ((BB == 1 || BB == 2 || BB == 3) && (CC == 6 || CC == 10))
            return Ца3_1;
        else if ((BB == 1 || BB == 2 || BB == 3) && (CC == 8))
            return Ца3_2;
        else
            return Ца3_3;
    }

    private double getКфаль() {
        if (BB == 8 || BB == 10)
            return 1;
        else
            return 0;
    }

    private double getСП1() {
        double Ца3, Кфаль, Цфаль;

        Ца3 = getЦа3();
        Кфаль = getКфаль();
        Цфаль = Цфальц_1;

        return (DD / Ккрс) * (Ца3 + Кфаль * Цфаль);
    }

    private double getСП2() {
        double Ца3, Кламстр, Кфаль, Цламоблм, Кформмп, Кламобмп;

        Ца3 = getЦа3();

        if (G == 0)
            Кламстр = 0;
        else if (G == 1)
            Кламстр = Кламстр_1;
        else if (G == 2)
            Кламстр = Кламстр_2;
        else
            Кламстр = Кламстр_3;

        Кфаль = getКфаль();

        Цламоблм = Цламоблм_1;

        if (CC == 1 || CC == 2)
            Кформмп = 1;
        else if (CC == 3 || CC == 37)
            Кформмп = Кформмп_1;
        else if (CC == 5)
            Кформмп = Кформмп_2;
        else
            Кформмп = Кформмп_3;

        if (F == 0)
            Кламобмп = 0;
        else if (F == 1)
            Кламобмп = 1;
        else
            Кламобмп = Кламобмп_1;

        return (DD / Ккрс) * (Ца3 * Кламстр) + Кфаль * Цламоблм * Кформмп
                * Кламобмп;
    }

    private double getСпру() {
        double Кпру, Кформпру, Цпру, Кцоблпру;

        if (BB == 7)
            Кпру = 1;
        else
            Кпру = 0;

        if (CC == 1 || CC == 2)
            Кформпру = 1;
        else if (CC == 3 || CC == 37)
            Кформпру = Кформпру_1;
        else if (CC == 4 || CC == 5)
            Кформпру = Кформпру_2;
        else if (CC == 6 || CC == 7)
            Кформпру = Кформпру_3;
        else if (CC == 8)
            Кформпру = Кформпру_4;
        else
            Кформпру = Кформпру_5;

        Цпру = Цпру_1;

        if (EE == 51)
            Кцоблпру = 1;
        else if (EE > 51 && EE < 100)
            Кцоблпру = (Double) data.get("Кцоблпру_" + EE);
        else
            Кцоблпру = 0;

        return Кпру * Кформпру * Цпру * Кцоблпру;
    }

    private double getСстоб(double Кламоб) {
        double Кстоб, Кформстоб, Цстоб;

        if (BB == 01 || BB == 04)
            Кстоб = 1;
        else
            Кстоб = 0;

        if (CC == 1 || CC == 2)
            Кформстоб = 1;
        else if (CC == 3 || CC == 37)
            Кформстоб = Кформстоб_1;
        else if (CC == 4 || CC == 5)
            Кформстоб = Кформстоб_2;
        else if (CC == 6 || CC == 7)
            Кформстоб = Кформстоб_3;
        else if (CC == 8)
            Кформстоб = Кформстоб_4;
        else
            Кформстоб = Кформстоб_5;

        Цстоб = Цстоб_1;

        return Кламоб * Кстоб * Кформстоб * Цстоб;
    }

    private double getСплотт(double Кламоб) {
        double Кплотт, Кформплотт, Цплотт;

        if (BB == 2 || BB == 5)
            Кплотт = 1;
        else
            Кплотт = 0;

        if (CC == 0 || CC == 1 || CC == 2)
            Кформплотт = 1;
        else if (CC == 3 || CC == 37)
            Кформплотт = Кформплотт_1;
        else if (CC == 4 || CC == 5)
            Кформплотт = Кформплотт_2;
        else if (CC == 6 || CC == 7)
            Кформплотт = Кформплотт_3;
        else if (CC == 8)
            Кформплотт = Кформплотт_4;
        else
            Кформплотт = Кформплотт_5;

        Цплотт = Цплотт_1;

        return Кламоб * Кплотт * Кформплотт * Цплотт;
    }

    private double getСкож() {
        double Ккож, Кформ, Цкож, Кцоблкож;

        if (BB == 3 || BB == 6)
            Ккож = 1;
        else
            Ккож = 0;

        if (CC == 1 || CC == 2)
            Кформ = 1;
        else if (CC == 3 || CC == 37)
            Кформ = Кформ_1;
        else if (CC == 4 || CC == 5)
            Кформ = Кформ_2;
        else if (CC == 6 || CC == 7)
            Кформ = Кформ_3;
        else if (CC == 8)
            Кформ = Кформ_4;
        else
            Кформ = Кформ_5;

        Цкож = Цкож_1;

        if (EE == 11)
            Кцоблкож = 1;
        else if (EE > 11 && EE < 50)
            Кцоблкож = (Double) data.get("Кцоблкож_" + EE);
        else
            Кцоблкож = 0;

        return Ккож * Кформ * Цкож * Кцоблкож;
    }
}
