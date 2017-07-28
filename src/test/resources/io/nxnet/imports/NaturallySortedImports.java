/**
 * Legal notice here
 */
package io.nxnet.imports;
// import comment 1
import static org.junit.Assert.assertArrayEquals;
import io.nxnet.tomrun.execution.ExecutionNode;
//import io.nxnet.tomrun.execution.ExecutionNodeType;
import io.nxnet.tomrun.execution.ExecutionNodeWritter;
import static org.junit.Assert.*;
import java.io.PrintWriter;

/**
 * orphaned comment block in imports
 */

import java.util.Iterator;
/**
 * import comment block 1
 */
import org.junit.Rule;

// orphaned comment in imports

import org.junit.platform.runner.JUnitPlatform;

// import comment 2
import org.junit.Test; // inline import comment 1 
import org.junit.platform.runner.JUnitPlatform;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith; // inline import comment 3


import static org.junit.Assert.assertEquals;



import com.github.javaparser.ast.ImportDeclaration;






import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * 
 * @author nruzic
 *
 */
@RunWith(JUnitPlatform.class)
public class ImportsTest
{

    private static class ImportVisitor extends VoidVisitorAdapter<Integer>
    {

        @Override
        public void visit(ImportDeclaration i, Integer arg)
        {
            /**
             * orphaned comment block
             */
             
            // orphaned comment

            // TODO Auto-generated method stub
            System.out.print(i);
            //super.visit(i, arg);
        }
        
    }
}


//foo
