/*
 * Copyright (C) 2011, Mysema Ltd
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package com.mysema.scalagen

import japa.parser.ast.{ CompilationUnit, Node }
import japa.parser.ast.body._
import japa.parser.ast.stmt._
import japa.parser.ast.expr._
import japa.parser.ast.visitor._
import java.util.ArrayList
import UnitTransformer._

object Primitives extends Primitives

/**
 * Primitives modifies primitive type related constants and method calls
 */
class Primitives  extends UnitTransformerBase {
  
  private val TRUE = new BooleanLiteral(true)
  
  private val FALSE = new BooleanLiteral(false)
  
  private val primitives = Set("Boolean","Byte","Char","Double","Float","Integer","Long","Short")
  
  def transform(cu: CompilationUnit): CompilationUnit = {
    cu.accept(this, cu).asInstanceOf[CompilationUnit] 
  }  
  
  override def visit(n: FieldAccess, arg: CompilationUnit): Node = n match {
    case Field("Boolean", "TRUE") => TRUE
    case Field("Boolean", "FALSE") => FALSE
    case _ => super.visit(n, arg)
  }
  
//  override def visit(n: FieldAccess, arg: CompilationUnit): Node = {
//    if (n.getScope.toString == "Boolean") {
//      if (n.getField == "TRUE") return TRUE
//      else if (n.getField == "FALSE") return FALSE
//    }
//    n
//  }
  
  override def visit(n: MethodCall, arg: CompilationUnit): Node = n match {
    case Method(scope, "valueOf", a :: Nil) if primitives.contains(scope) => a.accept(this, arg)
    case _ => super.visit(n, arg)
  }
  
//  override def visit(n: MethodCall, arg: CompilationUnit): Node = {
//    if (n.getName == "valueOf" && n.getArgs.size == 1 
//      && n.getScope != null && primitives.contains(n.getScope.toString)) {
//      n.getArgs.get(0)
//    } else {
//      n
//    }
//  }
}  