package ms;

import org.apache.commons.math3.random.RandomGenerator;
import org.cmg.ml.sam.sim.*;		
import eu.quanticol.carma.simulator.*;
import eu.quanticol.carma.simulator.space.Location;
import eu.quanticol.carma.simulator.space.Node;
import eu.quanticol.carma.simulator.space.SpaceModel;
import eu.quanticol.carma.simulator.space.Tuple;
import eu.quanticol.carma.simulator.space.Edge;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.TreeSet;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cmg.ml.sam.sim.sampling.*;


public class AllTheWay extends CarmaModel {
	
	public AllTheWay() {
		generateAgentBehaviour( );
	}
	

	
	public static class __RECORD__Message implements Cloneable {
		
		public Integer __FIELD__ID;
		public Integer __FIELD__COUNTER;
		
		public __RECORD__Message( Integer __FIELD__ID,Integer __FIELD__COUNTER) {
			this.__FIELD__ID = __FIELD__ID;
			this.__FIELD__COUNTER = __FIELD__COUNTER;
		}
	
		public __RECORD__Message( __RECORD__Message record ) {
			this.__FIELD__ID = record.__FIELD__ID;
			this.__FIELD__COUNTER = record.__FIELD__COUNTER;
		}
		
		public String toString() {
			return "[ "+"ID="+__FIELD__ID+" , "+"COUNTER="+__FIELD__COUNTER+" ]";
		}
		
		public boolean equals( Object o ) {
			if (o instanceof __RECORD__Message) {
				__RECORD__Message other = (__RECORD__Message) o;
				return 
				this.__FIELD__ID.equals( other.__FIELD__ID )					&&
				this.__FIELD__COUNTER.equals( other.__FIELD__COUNTER )					
						;	
			}	
			return false;
		}
		
		public __RECORD__Message clone() {
			return new __RECORD__Message( this );
		}
	}

	public final int __CONST__N = 20;

	public __RECORD__Message __FUN__newMessage ( 
		Integer __VARIABLE__id,Integer __VARIABLE__counter
	) {
		{
			//
			__RECORD__Message __VARIABLE__mess =new __RECORD__Message( Integer.valueOf( __VARIABLE__id ),
			Integer.valueOf( __VARIABLE__counter )
			 );
			//
			//
			return __VARIABLE__mess.clone();
			//
		}
	}
	
	
	
	/* START COMPONENT: Agent         */
	
	/* DEFINITIONS OF PROCESSES */
	public final CarmaProcessAutomaton _COMP_Agent = new CarmaProcessAutomaton("Agent");
	
	public final CarmaProcessAutomaton.State __STATE___Agent_ASLEEP = _COMP_Agent.newState("ASLEEP");		
	public final CarmaProcessAutomaton.State __STATE___Agent_INITIALIZE = _COMP_Agent.newState("INITIALIZE");		
	public final CarmaProcessAutomaton.State __STATE___Agent_AWAKE = _COMP_Agent.newState("AWAKE");		
	public final CarmaProcessAutomaton.State __STATE___Agent_CHECK = _COMP_Agent.newState("CHECK");		
	public final CarmaProcessAutomaton.State __STATE___Agent_LEADER = _COMP_Agent.newState("LEADER");		
	public final CarmaProcessAutomaton.State __STATE___Agent_FOLLOWER = _COMP_Agent.newState("FOLLOWER");		
	
	private void generateAgentBehaviour( ) {
		
		
		{
			CarmaAction action = new CarmaOutput(
				__ACT_NAME__send , __ACT__send , false  		
			) {
				
				@Override
				protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
					LinkedList<Object> toReturn = new LinkedList<Object>();
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					Integer __MY__id = (Integer) store.get( "id" );
					Integer __MY__counter = (Integer) store.get( "counter" );
					toReturn.add( __MY__id );
					toReturn.add( __MY__counter );
					return toReturn;
				}
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							final Node __MY__loc = store.get( "loc" , Node.class );
							final Node __ATTR__loc = store.get( "loc" , Node.class );
							Integer __MY__id = (Integer) store.get( "id" );
							store.set( "minimum", __MY__id );
						}
					};
				}
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
					final Node __MY__loc = myStore.get( "loc" , Node.class );
					Integer __MY__right = (Integer) myStore.get( "right" );
					return new CarmaPredicate() {
			
						//@Override
						public boolean satisfy(double now,CarmaStore store) {
							try {
								Node __ATTR__loc = store.get( "loc" , Node.class );
								Integer __ATTR__position = (Integer) store.get( "position" );
								return carmaEquals( __MY__right , __ATTR__position );
							} catch (NullPointerException e) {
								return false;
							}
						}
						
					};
					
				}
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_ASLEEP , 
				action , 
				__STATE___Agent_AWAKE );			
		}
		{
			CarmaAction action = new CarmaInput( 
				__ACT_NAME__send , __ACT__send , false  		
			) {
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys, final Object value, final double now) {
					
					LinkedList<Object> message = (LinkedList<Object>) value;
					final int __VARIABLE__rId = (Integer) message.get(0);
					final int __VARIABLE__rCounter = (Integer) message.get(1);
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							Node __MY__loc = store.get( "loc" , Node.class );
							Node __ATTR__loc = store.get( "loc" , Node.class );
							LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
							__RECORD__Message __MY__received = (__RECORD__Message) store.get( "received" );
							store.set( "received", __FUN__newMessage( 
										Integer.valueOf( __VARIABLE__rId ),
										Integer.valueOf( __VARIABLE__rCounter )
									).clone() );
							__MY__received = __FUN__newMessage( 
										Integer.valueOf( __VARIABLE__rId ),
										Integer.valueOf( __VARIABLE__rCounter )
									).clone();
							store.set( "buf", concatenate( __MY__buf , getList( __MY__received )  ) );
							__MY__buf = concatenate( __MY__buf , getList( __MY__received )  );
						}
					};
								
				}	
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, CarmaStore myStore, Object value) {
							LinkedList<Object> message = (LinkedList<Object>) value;
							final int __VARIABLE__rId = (Integer) message.get(0);
							final int __VARIABLE__rCounter = (Integer) message.get(1);
							final Node __MY__loc = myStore.get( "loc" , Node.class );
							Integer __MY__left = (Integer) myStore.get( "left" );
							return new CarmaPredicate() {
			
								//@Override
								public boolean satisfy(double now,CarmaStore store) {
									try {
										Node __ATTR__loc = store.get( "loc" , Node.class );
										Integer __ATTR__position = (Integer) store.get( "position" );
										return carmaEquals( __MY__left , __ATTR__position );
									} catch (NullPointerException e) {
										return false;
									}
								}
								
							};
					
				}
							
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_ASLEEP , 
				action , 
				__STATE___Agent_INITIALIZE );			
		}
		{
			CarmaAction action = new CarmaOutput(
				__ACT_NAME__send , __ACT__send , false  		
			) {
				
				@Override
				protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
					LinkedList<Object> toReturn = new LinkedList<Object>();
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					Integer __MY__id = (Integer) store.get( "id" );
					Integer __MY__counter = (Integer) store.get( "counter" );
					toReturn.add( __MY__id );
					toReturn.add( __MY__counter );
					return toReturn;
				}
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							final Node __MY__loc = store.get( "loc" , Node.class );
							final Node __ATTR__loc = store.get( "loc" , Node.class );
							Integer __MY__id = (Integer) store.get( "id" );
							store.set( "minimum", __MY__id );
						}
					};
				}
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
					final Node __MY__loc = myStore.get( "loc" , Node.class );
					Integer __MY__right = (Integer) myStore.get( "right" );
					return new CarmaPredicate() {
			
						//@Override
						public boolean satisfy(double now,CarmaStore store) {
							try {
								Node __ATTR__loc = store.get( "loc" , Node.class );
								Integer __ATTR__position = (Integer) store.get( "position" );
								return carmaEquals( __MY__right , __ATTR__position );
							} catch (NullPointerException e) {
								return false;
							}
						}
						
					};
					
				}
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_INITIALIZE , 
				action , 
				__STATE___Agent_AWAKE );			
		}
		{
			CarmaAction action = new CarmaInput( 
				__ACT_NAME__send , __ACT__send , false  		
			) {
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys, final Object value, final double now) {
					
					LinkedList<Object> message = (LinkedList<Object>) value;
					final int __VARIABLE__rId = (Integer) message.get(0);
					final int __VARIABLE__rCounter = (Integer) message.get(1);
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							Node __MY__loc = store.get( "loc" , Node.class );
							Node __ATTR__loc = store.get( "loc" , Node.class );
							LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
							__RECORD__Message __MY__received = (__RECORD__Message) store.get( "received" );
							store.set( "received", __FUN__newMessage( 
										Integer.valueOf( __VARIABLE__rId ),
										Integer.valueOf( __VARIABLE__rCounter )
									).clone() );
							__MY__received = __FUN__newMessage( 
										Integer.valueOf( __VARIABLE__rId ),
										Integer.valueOf( __VARIABLE__rCounter )
									).clone();
							store.set( "buf", concatenate( __MY__buf , getList( __MY__received )  ) );
							__MY__buf = concatenate( __MY__buf , getList( __MY__received )  );
						}
					};
								
				}	
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, CarmaStore myStore, Object value) {
							LinkedList<Object> message = (LinkedList<Object>) value;
							final int __VARIABLE__rId = (Integer) message.get(0);
							final int __VARIABLE__rCounter = (Integer) message.get(1);
							final Node __MY__loc = myStore.get( "loc" , Node.class );
							Integer __MY__left = (Integer) myStore.get( "left" );
							return new CarmaPredicate() {
			
								//@Override
								public boolean satisfy(double now,CarmaStore store) {
									try {
										Node __ATTR__loc = store.get( "loc" , Node.class );
										Integer __ATTR__position = (Integer) store.get( "position" );
										return carmaEquals( __MY__left , __ATTR__position );
									} catch (NullPointerException e) {
										return false;
									}
								}
								
							};
					
				}
							
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_INITIALIZE , 
				action , 
				__STATE___Agent_INITIALIZE );			
		}
		{
			CarmaAction action = new CarmaInput( 
				__ACT_NAME__send , __ACT__send , false  		
			) {
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys, final Object value, final double now) {
					
					LinkedList<Object> message = (LinkedList<Object>) value;
					final int __VARIABLE__rId = (Integer) message.get(0);
					final int __VARIABLE__rCounter = (Integer) message.get(1);
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							Node __MY__loc = store.get( "loc" , Node.class );
							Node __ATTR__loc = store.get( "loc" , Node.class );
							LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
							__RECORD__Message __MY__received = (__RECORD__Message) store.get( "received" );
							store.set( "received", __FUN__newMessage( 
										Integer.valueOf( __VARIABLE__rId ),
										Integer.valueOf( __VARIABLE__rCounter )
									).clone() );
							__MY__received = __FUN__newMessage( 
										Integer.valueOf( __VARIABLE__rId ),
										Integer.valueOf( __VARIABLE__rCounter )
									).clone();
							store.set( "buf", concatenate( __MY__buf , getList( __MY__received )  ) );
							__MY__buf = concatenate( __MY__buf , getList( __MY__received )  );
						}
					};
								
				}	
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, CarmaStore myStore, Object value) {
							LinkedList<Object> message = (LinkedList<Object>) value;
							final int __VARIABLE__rId = (Integer) message.get(0);
							final int __VARIABLE__rCounter = (Integer) message.get(1);
							final Node __MY__loc = myStore.get( "loc" , Node.class );
							Integer __MY__left = (Integer) myStore.get( "left" );
							return new CarmaPredicate() {
			
								//@Override
								public boolean satisfy(double now,CarmaStore store) {
									try {
										Node __ATTR__loc = store.get( "loc" , Node.class );
										Integer __ATTR__position = (Integer) store.get( "position" );
										return carmaEquals( __MY__left , __ATTR__position );
									} catch (NullPointerException e) {
										return false;
									}
								}
								
							};
					
				}
							
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_AWAKE , 
				action , 
				__STATE___Agent_AWAKE );			
		}
		{
			CarmaPredicate _FOO_predicate0 = new CarmaPredicate() {
		
				//@Override
				public boolean satisfy(double now,CarmaStore store) {
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
					Integer __MY__id = (Integer) store.get( "id" );
					Boolean __MY__known = (Boolean) store.get( "known" );
					return ( ( ( computeSize( __MY__buf ) )>( 0 ) )&&( !( carmaEquals( get(__MY__buf,0).__FIELD__ID , __MY__id ) ) ) )&&( !( __MY__known ) );
				}
					
			};
			{
				CarmaAction action = new CarmaOutput(
					__ACT_NAME__send , __ACT__send , false  		
				) {
					
					@Override
					protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
						LinkedList<Object> toReturn = new LinkedList<Object>();
						final Node __MY__loc = store.get( "loc" , Node.class );
						final Node __ATTR__loc = store.get( "loc" , Node.class );
						LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
						toReturn.add( get(__MY__buf,0).__FIELD__ID );
						toReturn.add( ( get(__MY__buf,0).__FIELD__COUNTER )+( 1 ) );
						return toReturn;
					}
					
					@Override
					protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
						return new CarmaStoreUpdate() {
							
							//@Override
							public void update(RandomGenerator r, CarmaStore store) {
								final Node __MY__loc = store.get( "loc" , Node.class );
								final Node __ATTR__loc = store.get( "loc" , Node.class );
								Integer __MY__minimum = (Integer) store.get( "minimum" );
								LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
								Integer __MY__counter = (Integer) store.get( "counter" );
								store.set( "minimum", Math.min( __MY__minimum , get(__MY__buf,0).__FIELD__ID ) );
								__MY__minimum = Math.min( __MY__minimum , get(__MY__buf,0).__FIELD__ID );
								store.set( "counter", ( __MY__counter )+( 1 ) );
								__MY__counter = ( __MY__counter )+( 1 );
								store.set( "buf", tail( __MY__buf ) );
								__MY__buf = tail( __MY__buf );
							}
						};
					}
					
					@Override
					protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
						final Node __MY__loc = myStore.get( "loc" , Node.class );
						Integer __MY__right = (Integer) myStore.get( "right" );
						return new CarmaPredicate() {
				
							//@Override
							public boolean satisfy(double now,CarmaStore store) {
								try {
									Node __ATTR__loc = store.get( "loc" , Node.class );
									Integer __ATTR__position = (Integer) store.get( "position" );
									return carmaEquals( __MY__right , __ATTR__position );
								} catch (NullPointerException e) {
									return false;
								}
							}
							
						};
						
					}
				};		
				
				_COMP_Agent.addTransition( 
					__STATE___Agent_AWAKE , 
					new CarmaPredicate.Conjunction(  _FOO_predicate0  ) , 
					action , 
					__STATE___Agent_AWAKE );			
			}
		}
		{
			CarmaPredicate _FOO_predicate0 = new CarmaPredicate() {
		
				//@Override
				public boolean satisfy(double now,CarmaStore store) {
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
					Integer __MY__id = (Integer) store.get( "id" );
					Boolean __MY__known = (Boolean) store.get( "known" );
					return ( ( ( computeSize( __MY__buf ) )>( 0 ) )&&( !( carmaEquals( get(__MY__buf,0).__FIELD__ID , __MY__id ) ) ) )&&( __MY__known );
				}
					
			};
			{
				CarmaAction action = new CarmaOutput(
					__ACT_NAME__check , __ACT__check , true  		
				) {
					
					@Override
					protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
						LinkedList<Object> toReturn = new LinkedList<Object>();
						final Node __MY__loc = store.get( "loc" , Node.class );
						final Node __ATTR__loc = store.get( "loc" , Node.class );
						return toReturn;
					}
					
					@Override
					protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
						return new CarmaStoreUpdate() {
							
							//@Override
							public void update(RandomGenerator r, CarmaStore store) {
								final Node __MY__loc = store.get( "loc" , Node.class );
								final Node __ATTR__loc = store.get( "loc" , Node.class );
								Integer __MY__minimum = (Integer) store.get( "minimum" );
								LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
								Integer __MY__counter = (Integer) store.get( "counter" );
								store.set( "minimum", Math.min( __MY__minimum , get(__MY__buf,0).__FIELD__ID ) );
								__MY__minimum = Math.min( __MY__minimum , get(__MY__buf,0).__FIELD__ID );
								store.set( "counter", ( __MY__counter )+( 1 ) );
								__MY__counter = ( __MY__counter )+( 1 );
								store.set( "buf", tail( __MY__buf ) );
								__MY__buf = tail( __MY__buf );
							}
						};
					}
					
					@Override
					protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
						return CarmaPredicate.FALSE;
						
					}
				};		
				
				_COMP_Agent.addTransition( 
					__STATE___Agent_AWAKE , 
					new CarmaPredicate.Conjunction(  _FOO_predicate0  ) , 
					action , 
					__STATE___Agent_CHECK );			
			}
		}
		{
			CarmaPredicate _FOO_predicate0 = new CarmaPredicate() {
		
				//@Override
				public boolean satisfy(double now,CarmaStore store) {
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
					Integer __MY__id = (Integer) store.get( "id" );
					return ( ( computeSize( __MY__buf ) )>( 0 ) )&&( carmaEquals( get(__MY__buf,0).__FIELD__ID , __MY__id ) );
				}
					
			};
			{
				CarmaAction action = new CarmaOutput(
					__ACT_NAME__check , __ACT__check , true  		
				) {
					
					@Override
					protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
						LinkedList<Object> toReturn = new LinkedList<Object>();
						final Node __MY__loc = store.get( "loc" , Node.class );
						final Node __ATTR__loc = store.get( "loc" , Node.class );
						return toReturn;
					}
					
					@Override
					protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
						return new CarmaStoreUpdate() {
							
							//@Override
							public void update(RandomGenerator r, CarmaStore store) {
								final Node __MY__loc = store.get( "loc" , Node.class );
								final Node __ATTR__loc = store.get( "loc" , Node.class );
								LinkedList<__RECORD__Message> __MY__buf = (LinkedList<__RECORD__Message>) store.get( "buf" );
								store.set( "ringSize", get(__MY__buf,0).__FIELD__COUNTER );
								store.set( "known", true );
								store.set( "buf", tail( __MY__buf ) );
								__MY__buf = tail( __MY__buf );
							}
						};
					}
					
					@Override
					protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
						return CarmaPredicate.FALSE;
						
					}
				};		
				
				_COMP_Agent.addTransition( 
					__STATE___Agent_AWAKE , 
					new CarmaPredicate.Conjunction(  _FOO_predicate0  ) , 
					action , 
					__STATE___Agent_CHECK );			
			}
		}
		{
			CarmaPredicate _FOO_predicate0 = new CarmaPredicate() {
		
				//@Override
				public boolean satisfy(double now,CarmaStore store) {
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					Integer __MY__counter = (Integer) store.get( "counter" );
					Integer __MY__ringSize = (Integer) store.get( "ringSize" );
					Integer __MY__id = (Integer) store.get( "id" );
					Integer __MY__minimum = (Integer) store.get( "minimum" );
					return ( carmaEquals( __MY__counter , __MY__ringSize ) )&&( carmaEquals( __MY__id , __MY__minimum ) );
				}
					
			};
			{
				CarmaAction action = new CarmaOutput(
					__ACT_NAME__youWin , __ACT__youWin , true  		
				) {
					
					@Override
					protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
						LinkedList<Object> toReturn = new LinkedList<Object>();
						final Node __MY__loc = store.get( "loc" , Node.class );
						final Node __ATTR__loc = store.get( "loc" , Node.class );
						return toReturn;
					}
					
					@Override
					protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
						return new CarmaStoreUpdate() {
							
							//@Override
							public void update(RandomGenerator r, CarmaStore store) {
								final Node __MY__loc = store.get( "loc" , Node.class );
								final Node __ATTR__loc = store.get( "loc" , Node.class );
								store.set( "leader", true );
							}
						};
					}
					
					@Override
					protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
						return CarmaPredicate.FALSE;
						
					}
				};		
				
				_COMP_Agent.addTransition( 
					__STATE___Agent_CHECK , 
					new CarmaPredicate.Conjunction(  _FOO_predicate0  ) , 
					action , 
					__STATE___Agent_LEADER );			
			}
		}
		{
			CarmaPredicate _FOO_predicate0 = new CarmaPredicate() {
		
				//@Override
				public boolean satisfy(double now,CarmaStore store) {
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					Integer __MY__counter = (Integer) store.get( "counter" );
					Integer __MY__ringSize = (Integer) store.get( "ringSize" );
					Integer __MY__id = (Integer) store.get( "id" );
					Integer __MY__minimum = (Integer) store.get( "minimum" );
					return ( carmaEquals( __MY__counter , __MY__ringSize ) )&&( !( carmaEquals( __MY__id , __MY__minimum ) ) );
				}
					
			};
			{
				CarmaAction action = new CarmaOutput(
					__ACT_NAME__youLose , __ACT__youLose , true  		
				) {
					
					@Override
					protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
						LinkedList<Object> toReturn = new LinkedList<Object>();
						final Node __MY__loc = store.get( "loc" , Node.class );
						final Node __ATTR__loc = store.get( "loc" , Node.class );
						return toReturn;
					}
					
					@Override
					protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
						return new CarmaStoreUpdate() {
							
							//@Override
							public void update(RandomGenerator r, CarmaStore store) {
								final Node __MY__loc = store.get( "loc" , Node.class );
								final Node __ATTR__loc = store.get( "loc" , Node.class );
							}
						};
					}
					
					@Override
					protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
						return CarmaPredicate.FALSE;
						
					}
				};		
				
				_COMP_Agent.addTransition( 
					__STATE___Agent_CHECK , 
					new CarmaPredicate.Conjunction(  _FOO_predicate0  ) , 
					action , 
					__STATE___Agent_FOLLOWER );			
			}
		}
		{
			CarmaAction action = new CarmaOutput(
				__ACT_NAME__winner , __ACT__winner , true  		
			) {
				
				@Override
				protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
					LinkedList<Object> toReturn = new LinkedList<Object>();
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					return toReturn;
				}
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							final Node __MY__loc = store.get( "loc" , Node.class );
							final Node __ATTR__loc = store.get( "loc" , Node.class );
						}
					};
				}
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
					return CarmaPredicate.FALSE;
					
				}
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_LEADER , 
				action , 
				__STATE___Agent_LEADER );			
		}
		{
			CarmaAction action = new CarmaOutput(
				__ACT_NAME__loser , __ACT__loser , true  		
			) {
				
				@Override
				protected Object getValue(CarmaSystem sys, CarmaStore store, final double now) {
					LinkedList<Object> toReturn = new LinkedList<Object>();
					final Node __MY__loc = store.get( "loc" , Node.class );
					final Node __ATTR__loc = store.get( "loc" , Node.class );
					return toReturn;
				}
				
				@Override
				protected CarmaStoreUpdate getUpdate(CarmaSystem sys,  final double now ) {
					return new CarmaStoreUpdate() {
						
						//@Override
						public void update(RandomGenerator r, CarmaStore store) {
							final Node __MY__loc = store.get( "loc" , Node.class );
							final Node __ATTR__loc = store.get( "loc" , Node.class );
						}
					};
				}
				
				@Override
				protected CarmaPredicate getPredicate(CarmaSystem sys, final CarmaStore myStore) {
					return CarmaPredicate.FALSE;
					
				}
			};		
			
			_COMP_Agent.addTransition( 
				__STATE___Agent_FOLLOWER , 
				action , 
				__STATE___Agent_FOLLOWER );			
		}
		
	}
	
	public CarmaComponent createComponentAgent( 
		Integer __VARIABLE__position, Integer __VARIABLE__id  
	) {
		CarmaComponent c = new CarmaComponent();
		c.setName( "Agent" );
		Integer __ATTR__position;
		Integer __MY__position;
		__ATTR__position =  __VARIABLE__position;
		__MY__position = __ATTR__position;
		c.set( "position" ,  __ATTR__position );
		Integer __ATTR__id;
		Integer __MY__id;
		__ATTR__id =  __VARIABLE__id;
		__MY__id = __ATTR__id;
		c.set( "id" ,  __ATTR__id );
		Integer __ATTR__counter;
		Integer __MY__counter;
		__ATTR__counter =  0;
		__MY__counter = __ATTR__counter;
		c.set( "counter" ,  __ATTR__counter );
		Integer __ATTR__ringSize;
		Integer __MY__ringSize;
		__ATTR__ringSize =  1;
		__MY__ringSize = __ATTR__ringSize;
		c.set( "ringSize" ,  __ATTR__ringSize );
		Integer __ATTR__right;
		Integer __MY__right;
		__ATTR__right =  ( ( __ATTR__position )+( 1 ) )%( __CONST__N );
		__MY__right = __ATTR__right;
		c.set( "right" ,  __ATTR__right );
		Integer __ATTR__left;
		Integer __MY__left;
		__ATTR__left =  ( ( ( __ATTR__position )+( __CONST__N ) )-( 1 ) )%( __CONST__N );
		__MY__left = __ATTR__left;
		c.set( "left" ,  __ATTR__left );
		Integer __ATTR__minimum;
		Integer __MY__minimum;
		__ATTR__minimum =  10000;
		__MY__minimum = __ATTR__minimum;
		c.set( "minimum" ,  __ATTR__minimum );
		Boolean __ATTR__known;
		Boolean __MY__known;
		__ATTR__known =  false;
		__MY__known = __ATTR__known;
		c.set( "known" ,  __ATTR__known );
		LinkedList<__RECORD__Message> __ATTR__buf;
		LinkedList<__RECORD__Message> __MY__buf;
		__ATTR__buf =  new LinkedList<__RECORD__Message>()
		;
		__MY__buf = __ATTR__buf;
		c.set( "buf" ,  __ATTR__buf );
		__RECORD__Message __ATTR__received;
		__RECORD__Message __MY__received;
		__ATTR__received =  new __RECORD__Message( Integer.valueOf( 0 ),
		Integer.valueOf( 0 )
		 );
		__MY__received = __ATTR__received;
		c.set( "received" ,  __ATTR__received );
		Boolean __ATTR__leader;
		Boolean __MY__leader;
		__ATTR__leader =  false;
		__MY__leader = __ATTR__leader;
		c.set( "leader" ,  __ATTR__leader );
		c.addAgent( new CarmaSequentialProcess( c , _COMP_Agent , __STATE___Agent_ASLEEP ));
		return c;
	}	
	
	/* END COMPONENT: Agent */
		
	
	public static final int __ACT__send = 0;	
	public static final String __ACT_NAME__send = "send";
	public static final int __ACT__check = 1;	
	public static final String __ACT_NAME__check = "check";
	public static final int __ACT__youWin = 2;	
	public static final String __ACT_NAME__youWin = "youWin";
	public static final int __ACT__youLose = 3;	
	public static final String __ACT_NAME__youLose = "youLose";
	public static final int __ACT__winner = 4;	
	public static final String __ACT_NAME__winner = "winner";
	public static final int __ACT__loser = 5;	
	public static final String __ACT_NAME__loser = "loser";
	
	
	public String[] getSystems() {
		return new String[] {
			"Ring"
		};	
	}
	
	public SimulationFactory<CarmaSystem> getFactory( String name ) {
		if ("Ring".equals( name )) {
			return getFactorySystemRing();
		}
		return null;
	}
			
	
	public class __SYSTEM__Ring extends CarmaSystem {
		
		public __SYSTEM__Ring( ) {
			super( );
			Integer __ATTR__messages;
			Integer __GLOBAL__messages;
			__ATTR__messages =  0;
			__GLOBAL__messages = __ATTR__messages;
			setGLobalAttribute( "messages" , __ATTR__messages );
			CarmaSystem system = this;
			CarmaSystem sys = this;
			for( int __VARIABLE__i = 0; ( __VARIABLE__i )<( __CONST__N ) ; __VARIABLE__i = __VARIABLE__i + 1 ) {
				{
						CarmaComponent fooComponent = createComponentAgent(					
							Integer.valueOf( __VARIABLE__i ),
							Integer.valueOf( ( 10 )*( __VARIABLE__i ) )
						);
						system.addComponent( fooComponent );
				}
			}
		}
		
		@Override
		public double broadcastProbability( CarmaStore sender , CarmaStore receiver , int action ) {
			return 1.0;
		}
	
		@Override
		public double unicastProbability( CarmaStore sender , CarmaStore receiver , int action ) {
			return 1.0;
		}
		
		@Override
		public double broadcastRate( CarmaStore sender , int action ) {
			return 1.0;
		}
	
		@Override
		public double unicastRate( CarmaStore sender , int action ) {
			return 1.0;
		}				
		
		@Override
		public void broadcastUpdate( 
			final RandomGenerator random , 
			final CarmaStore sender , 
			final int action , 
			final Object value ) {
			final CarmaSystem system = this;
			final CarmaSystem sys = this;
			final CarmaStore global = this.global;
			final CarmaStore store = this.global;
			Node __SENDER__loc = sender.get( "loc" , Node.class );
		}
		
		@Override
		public void unicastUpdate( 
			final RandomGenerator random , 
			final CarmaStore sender , 
			final CarmaStore receiver, 
			int action , 
			final Object value ) {
			final CarmaSystem system = this;
			final CarmaSystem sys = this;
			final CarmaStore global = this.global;
			final CarmaStore store = this.global;
			Integer __GLOBAL__messages = (Integer) global.get( "messages" );
			Node __SENDER__loc = sender.get( "loc" , Node.class );
			if (action==__ACT__send) {
				store.set( "messages", ( __GLOBAL__messages )+( 1 ) );
				return ;				
			}
		}		
	}
	
	
	public SimulationFactory<CarmaSystem> getFactorySystemRing() {
		return new SimulationFactory<CarmaSystem>() {
	
			//@Override
			public CarmaSystem getModel() {
				CarmaSystem sys = new __SYSTEM__Ring();
				CarmaSystem.setCurrentSpaceModel( sys.getSpaceModel() );
				return sys;
			}
		
			//@Override
			public Measure<CarmaSystem> getMeasure(String name) {
				// TODO Auto-generated method stub
				//FIXME!!!!
				return null;
			}
		
		};
		
	}
	
		
		public String[] getMeasures() {
			TreeSet<String> sortedSet = new TreeSet<String>( );
			sortedSet.add( "asleep" );
			sortedSet.add( "leader" );
			sortedSet.add( "followers" );
			sortedSet.add( "messages" );
			return sortedSet.toArray( new String[ sortedSet.size() ] );
		}
		
		public Measure<CarmaSystem> getMeasure( String name , Map<String,Object> parameters ) {
			if ("asleep".equals( name ) ) {
				return getMeasureasleep( parameters );
			}
			if ("leader".equals( name ) ) {
				return getMeasureleader( parameters );
			}
			if ("followers".equals( name ) ) {
				return getMeasurefollowers( parameters );
			}
			if ("messages".equals( name ) ) {
				return getMeasuremessages( parameters );
			}
			return null;
		}
	
		public String[] getMeasureParameters( String name ) {
			if ("asleep".equals( name ) ) {
				return new String[] { };
			}
			if ("leader".equals( name ) ) {
				return new String[] { };
			}
			if ("followers".equals( name ) ) {
				return new String[] { };
			}
			if ("messages".equals( name ) ) {
				return new String[] { };
			}
			return new String[] {};
		}
		
		public Map<String,Class<?>> getParametersType( String name ) {
			if ("asleep".equals( name ) ) {
				HashMap<String,Class<?>> toReturn = new HashMap<>();
				return toReturn;
			}
			if ("leader".equals( name ) ) {
				HashMap<String,Class<?>> toReturn = new HashMap<>();
				return toReturn;
			}
			if ("followers".equals( name ) ) {
				HashMap<String,Class<?>> toReturn = new HashMap<>();
				return toReturn;
			}
			if ("messages".equals( name ) ) {
				HashMap<String,Class<?>> toReturn = new HashMap<>();
				return toReturn;
			}
			return new HashMap<>();
		}
		
	
		private double __MEASURE__asleep( CarmaSystem system ) {
			final CarmaStore global = system.getGlobalStore();
			final double now = system.now();
			final CarmaSystem sys = system;
			return system.measure( 
				new BasicComponentPredicate(
					new CarmaPredicate() {
						
						//Here we assume that the following "final" references are available (if needed):
						//- global: reference to the global store;
						//- sender: reference to the store of sender;
						//- receiver: reference to the store of the receiver;				
						//@Override
						public boolean satisfy(double now,CarmaStore store) {
							Node __MY__loc = store.get( "loc" , Node.class );
							try{
								Boolean result = true;
								return (result==null?false:result);
							} catch (NullPointerException e) {
								return false;
							}
						}
					
						
					}
					, new CarmaProcessPredicate() {
				
						//@Override
						public boolean eval(CarmaProcess p) {
							if (p instanceof CarmaSequentialProcess) {
								CarmaSequentialProcess csp = (CarmaSequentialProcess) p;
								try{
									return csp.getName().equals("Agent")&&csp.getState().getName().equals("ASLEEP");
								} catch (NullPointerException e) {
									return false;
								}
							}
							return false;
						}
									
					}
					)
			)
			;
		}
		
		
		private Measure<CarmaSystem> getMeasureasleep( 
			Map<String,Object> parameters
		) {
			
		
			return new Measure<CarmaSystem>() {
			
				//@Override
				public double measure(final CarmaSystem system) {
					return __MEASURE__asleep( system );
				}
		
				//@Override
				public String getName() {
					return "asleep";
				}
			
			};
			
		}
		
		private double __MEASURE__leader( CarmaSystem system ) {
			final CarmaStore global = system.getGlobalStore();
			final double now = system.now();
			final CarmaSystem sys = system;
			return system.measure( 
				new BasicComponentPredicate(
					new CarmaPredicate() {
						
						//Here we assume that the following "final" references are available (if needed):
						//- global: reference to the global store;
						//- sender: reference to the store of sender;
						//- receiver: reference to the store of the receiver;				
						//@Override
						public boolean satisfy(double now,CarmaStore store) {
							Node __MY__loc = store.get( "loc" , Node.class );
							try{
								Boolean result = true;
								return (result==null?false:result);
							} catch (NullPointerException e) {
								return false;
							}
						}
					
						
					}
					, new CarmaProcessPredicate() {
				
						//@Override
						public boolean eval(CarmaProcess p) {
							if (p instanceof CarmaSequentialProcess) {
								CarmaSequentialProcess csp = (CarmaSequentialProcess) p;
								try{
									return csp.getName().equals("Agent")&&csp.getState().getName().equals("LEADER");
								} catch (NullPointerException e) {
									return false;
								}
							}
							return false;
						}
									
					}
					)
			)
			;
		}
		
		
		private Measure<CarmaSystem> getMeasureleader( 
			Map<String,Object> parameters
		) {
			
		
			return new Measure<CarmaSystem>() {
			
				//@Override
				public double measure(final CarmaSystem system) {
					return __MEASURE__leader( system );
				}
		
				//@Override
				public String getName() {
					return "leader";
				}
			
			};
			
		}
		
		private double __MEASURE__followers( CarmaSystem system ) {
			final CarmaStore global = system.getGlobalStore();
			final double now = system.now();
			final CarmaSystem sys = system;
			return system.measure( 
				new BasicComponentPredicate(
					new CarmaPredicate() {
						
						//Here we assume that the following "final" references are available (if needed):
						//- global: reference to the global store;
						//- sender: reference to the store of sender;
						//- receiver: reference to the store of the receiver;				
						//@Override
						public boolean satisfy(double now,CarmaStore store) {
							Node __MY__loc = store.get( "loc" , Node.class );
							try{
								Boolean result = true;
								return (result==null?false:result);
							} catch (NullPointerException e) {
								return false;
							}
						}
					
						
					}
					, new CarmaProcessPredicate() {
				
						//@Override
						public boolean eval(CarmaProcess p) {
							if (p instanceof CarmaSequentialProcess) {
								CarmaSequentialProcess csp = (CarmaSequentialProcess) p;
								try{
									return csp.getName().equals("Agent")&&csp.getState().getName().equals("FOLLOWER");
								} catch (NullPointerException e) {
									return false;
								}
							}
							return false;
						}
									
					}
					)
			)
			;
		}
		
		
		private Measure<CarmaSystem> getMeasurefollowers( 
			Map<String,Object> parameters
		) {
			
		
			return new Measure<CarmaSystem>() {
			
				//@Override
				public double measure(final CarmaSystem system) {
					return __MEASURE__followers( system );
				}
		
				//@Override
				public String getName() {
					return "followers";
				}
			
			};
			
		}
		
		private double __MEASURE__messages( CarmaSystem system ) {
			final CarmaStore global = system.getGlobalStore();
			final double now = system.now();
			final CarmaSystem sys = system;
			Integer __GLOBAL__messages = (Integer) global.get( "messages" );
			return __GLOBAL__messages;
		}
		
		
		private Measure<CarmaSystem> getMeasuremessages( 
			Map<String,Object> parameters
		) {
			
		
			return new Measure<CarmaSystem>() {
			
				//@Override
				public double measure(final CarmaSystem system) {
					return __MEASURE__messages( system );
				}
		
				//@Override
				public String getName() {
					return "messages";
				}
			
			};
			
		}
		
		
	
}
