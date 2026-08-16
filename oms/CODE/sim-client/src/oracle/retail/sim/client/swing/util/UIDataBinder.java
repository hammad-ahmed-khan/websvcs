package oracle.retail.sim.client.swing.util;

import oracle.retail.sim.common.core.type.Displayable;

/******************************************************************************************
 * This class exclusively exists to support building GUI Business objects that wraps
 * multiple data objects and supports a clear API. For example, RcomOrderLine wraps both
 * an Order Line and a Return Line in the RCOM application space to present a singluar API
 * on the client. VOs (or Value Objects) in the platform environment are horrendous at
 * supporting client side logic and thus need wrappers to support basic functionality (see
 * RSM for examples).
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public abstract class UIDataBinder implements Displayable {

}
