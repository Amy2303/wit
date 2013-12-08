// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.core.ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import webit.script.core.ast.loop.LoopInfo;
import webit.script.core.ast.statments.BlockStatment;
import webit.script.core.ast.statments.BlockStatmentNoLoops;
import webit.script.core.ast.statments.IBlockStatment;
import webit.script.core.VariantIndexer;
import webit.script.exceptions.ParseException;
import webit.script.util.ArrayUtil;
import webit.script.util.StatmentUtil;
import webit.script.util.StringUtil;

/**
 *
 * @author Zqq
 */
public final class StatmentList {

    private final List<Statment> statmentList;

    public StatmentList() {
        this.statmentList = new LinkedList<Statment>();
    }

    public StatmentList add(Statment stat) {
        if ((stat = StatmentUtil.optimize(stat)) != null) {
            statmentList.add(stat);
        }
        return this;
    }

    public Statment[] toInvertArray() {
        Statment[] statments = this.statmentList.toArray(new Statment[statmentList.size()]);
        ArrayUtil.invert(statments);
        return statments;
    }

    public TemplateAST popTemplateAST(Map<String, Integer> varIndexer) {

        Statment[] statments = this.toInvertArray();

        List<LoopInfo> loopInfos;
        if ((loopInfos = StatmentUtil.collectPossibleLoopsInfo(statments)) == null) {
            return new TemplateAST(VariantIndexer.getVariantIndexer(varIndexer), statments);
        } else {
            throw new ParseException("loop overflow: ".concat(StringUtil.join(loopInfos, ",")));
        }
    }

    public IBlockStatment popIBlockStatment(Map<String, Integer> varIndexer, int line, int column) {
        Statment[] statments;

        List<LoopInfo> loopInfoList = StatmentUtil.collectPossibleLoopsInfo(
                statments = this.toInvertArray());

        return loopInfoList != null
                ? new BlockStatment(VariantIndexer.getVariantIndexer(varIndexer), statments, loopInfoList.toArray(new LoopInfo[loopInfoList.size()]), line, column)
                : new BlockStatmentNoLoops(VariantIndexer.getVariantIndexer(varIndexer), statments, line, column);
    }

}
